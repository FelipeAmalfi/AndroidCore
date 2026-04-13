#!/usr/bin/env node

const fs = require("fs");
const path = require("path");
const crypto = require("crypto");
const { spawnSync } = require("child_process");

const repoRoot = path.resolve(__dirname, "..", "..");
const defaultConfigPath = path.resolve(__dirname, "config.json");

function log(message) {
  console.log(`[docs-agent] ${message}`);
}

function parseArgs(argv) {
  const parsed = {
    mode: "run",
    config: defaultConfigPath,
    from: "",
    to: "",
    allowPublishFailure: false
  };

  for (let index = 0; index < argv.length; index += 1) {
    const arg = argv[index];
    const next = argv[index + 1];

    if (arg === "--mode" && next) {
      parsed.mode = next;
      index += 1;
      continue;
    }

    if (arg === "--config" && next) {
      parsed.config = path.isAbsolute(next) ? next : path.resolve(repoRoot, next);
      index += 1;
      continue;
    }

    if (arg === "--from" && next) {
      parsed.from = next;
      index += 1;
      continue;
    }

    if (arg === "--to" && next) {
      parsed.to = next;
      index += 1;
      continue;
    }

    if (arg === "--allow-publish-failure") {
      parsed.allowPublishFailure = true;
    }
  }

  return parsed;
}

function readJson(filePath, fallback) {
  if (!fs.existsSync(filePath)) {
    return fallback;
  }
  return JSON.parse(fs.readFileSync(filePath, "utf8"));
}

function ensureDir(dirPath) {
  fs.mkdirSync(dirPath, { recursive: true });
}

function writeText(filePath, content) {
  ensureDir(path.dirname(filePath));
  fs.writeFileSync(filePath, content, "utf8");
}

function execGit(args) {
  const result = spawnSync("git", args, { cwd: repoRoot, encoding: "utf8" });
  if (result.status !== 0) {
    return "";
  }
  return (result.stdout || "").trim();
}

function normalizeDiffRange(from, to) {
  const zeros = /^0+$/;
  if (!from || !to || zeros.test(from) || zeros.test(to)) {
    return "";
  }
  return `${from}..${to}`;
}

function getChangedFiles(from, to) {
  const range = normalizeDiffRange(from, to);
  let output = "";

  if (range) {
    output = execGit(["diff", "--name-only", range]);
  } else if (process.env.GITHUB_EVENT_BEFORE && process.env.GITHUB_SHA) {
    output = execGit(["diff", "--name-only", `${process.env.GITHUB_EVENT_BEFORE}..${process.env.GITHUB_SHA}`]);
  } else if (process.env.CI) {
    output = execGit(["diff", "--name-only", "HEAD~1..HEAD"]);
  } else {
    output = execGit(["status", "--porcelain"])
      .split(/\r?\n/)
      .map((line) => line.trim())
      .filter(Boolean)
      .map((line) => line.substring(3));
    return output;
  }

  return output ? output.split(/\r?\n/).filter(Boolean) : [];
}

function getTrackedKotlinFiles() {
  const output = execGit(["ls-files", "*.kt"]);
  if (!output) {
    return [];
  }
  return output
    .split(/\r?\n/)
    .filter(Boolean)
    .filter((item) => !item.includes("/build/"));
}

function hasRelevantChanges(changedFiles) {
  return changedFiles.some((file) => {
    const normalized = file.replace(/\\/g, "/");
    return normalized.endsWith(".kt")
      || normalized.startsWith("design-system/")
      || normalized.includes("tokens/")
      || normalized.includes("agents/docs-agent/");
  });
}

function calculateSnapshot(files) {
  const hash = crypto.createHash("sha256");
  for (const relative of files.sort()) {
    const absolute = path.resolve(repoRoot, relative);
    if (!fs.existsSync(absolute)) {
      continue;
    }
    hash.update(relative);
    hash.update(fs.readFileSync(absolute));
  }
  return hash.digest("hex");
}

function scanKotlinFiles(baseDir) {
  if (!fs.existsSync(baseDir)) {
    return [];
  }

  const files = [];
  const entries = fs.readdirSync(baseDir, { withFileTypes: true });

  for (const entry of entries) {
    const fullPath = path.join(baseDir, entry.name);
    if (entry.isDirectory()) {
      if (entry.name === "build") {
        continue;
      }
      files.push(...scanKotlinFiles(fullPath));
      continue;
    }

    if (entry.isFile() && entry.name.endsWith(".kt")) {
      files.push(fullPath);
    }
  }

  return files;
}

function splitParameters(paramText) {
  const chunks = [];
  let current = "";
  let depth = 0;

  for (const ch of paramText) {
    if (ch === "(") depth += 1;
    if (ch === ")") depth -= 1;

    if (ch === "," && depth === 0) {
      if (current.trim()) {
        chunks.push(current.trim());
      }
      current = "";
    } else {
      current += ch;
    }
  }

  if (current.trim()) {
    chunks.push(current.trim());
  }

  return chunks;
}

function parseParameters(raw) {
  if (!raw.trim()) {
    return [];
  }

  return splitParameters(raw)
    .map((item) => item.replace(/\s+/g, " ").trim())
    .filter((item) => item.includes(":"))
    .map((item) => {
      const [left, right] = item.split(":");
      const [typePart, defaultPart] = right.split("=");
      return {
        name: left.trim(),
        type: (typePart || "").trim(),
        defaultValue: defaultPart ? defaultPart.trim() : ""
      };
    });
}

function extractKotlinComponents() {
  const sourceRoots = [
    path.resolve(repoRoot, "design-system", "src", "main"),
    path.resolve(repoRoot, "app", "src", "main")
  ];

  const components = [];
  const previewsByFile = new Map();

  for (const root of sourceRoots) {
    for (const file of scanKotlinFiles(root)) {
      const relativePath = path.relative(repoRoot, file).replace(/\\/g, "/");
      const content = fs.readFileSync(file, "utf8");

      const previewMatches = Array.from(content.matchAll(/@Preview[\s\S]{0,220}?fun\s+(\w+)\s*\(/g));
      previewsByFile.set(relativePath, previewMatches.map((match) => match[1]));

      const composableMatches = Array.from(content.matchAll(/@Composable[\s\S]{0,350}?fun\s+(\w+)\s*\(([\s\S]*?)\)\s*\{/g));
      for (const match of composableMatches) {
        const name = match[1];
        const params = parseParameters(match[2]);
        const stateParams = params.filter((param) => /state/i.test(param.name) || /State$/.test(param.type));

        components.push({
          name,
          path: relativePath,
          parameters: params,
          previews: previewsByFile.get(relativePath) || [],
          stateParameters: stateParams
        });
      }
    }
  }

  return components.sort((a, b) => a.name.localeCompare(b.name));
}

function extractColors(filePath) {
  if (!fs.existsSync(filePath)) {
    return { light: [], dark: [] };
  }

  const text = fs.readFileSync(filePath, "utf8");
  const result = { light: [], dark: [] };
  let mode = "";

  for (const line of text.split(/\r?\n/)) {
    if (line.includes("object Light")) {
      mode = "light";
      continue;
    }
    if (line.includes("object Dark")) {
      mode = "dark";
      continue;
    }

    const match = line.match(/val\s+([A-Za-z0-9_]+):\s*Color\s*=\s*colorFromHex\((0x[0-9A-Fa-f]+)L\)/);
    if (match && (mode === "light" || mode === "dark")) {
      result[mode].push({
        name: match[1],
        hex: `${match[2]}L`
      });
    }
  }

  return result;
}

function extractSpacing(filePath) {
  if (!fs.existsSync(filePath)) {
    return [];
  }

  const text = fs.readFileSync(filePath, "utf8");
  const defaultsBlockMatch = text.match(/fun\s+default\(\):\s*DSSpacing\s*=\s*DSSpacing\(([\s\S]*?)\)\s*\)/);
  const block = defaultsBlockMatch ? defaultsBlockMatch[1] : "";
  const values = [];

  for (const line of block.split(/\r?\n/)) {
    const match = line.match(/(\w+)\s*=\s*([0-9.]+)\.dp/);
    if (match) {
      values.push({ name: match[1], value: `${match[2]}dp` });
    }
  }

  return values;
}

function extractTypography(filePath) {
  if (!fs.existsSync(filePath)) {
    return [];
  }

  const text = fs.readFileSync(filePath, "utf8");
  const defaultsBlockMatch = text.match(/fun\s+default\(\):\s*DSTypography\s*=\s*DSTypography\(([\s\S]*?)\)\s*\)/);
  const block = defaultsBlockMatch ? defaultsBlockMatch[1] : "";
  const styles = [];

  for (const line of block.split(/\r?\n/)) {
    const match = line.match(/(\w+)\s*=\s*DSTextStyle\(([^)]*)\)/);
    if (match) {
      const parts = match[2].split(",").map((item) => item.trim());
      styles.push({
        name: match[1],
        fontSize: parts[0] || "",
        lineHeight: parts[1] || "",
        fontWeight: parts[2] || "",
        letterSpacing: parts[3] || ""
      });
    }
  }

  return styles;
}

function extractDesignTokens(config) {
  const colorsFile = path.resolve(repoRoot, config.tokenPaths.colors);
  const spacingFile = path.resolve(repoRoot, config.tokenPaths.spacing);
  const typographyFile = path.resolve(repoRoot, config.tokenPaths.typography);

  const tokens = {
    colors: extractColors(colorsFile),
    spacing: extractSpacing(spacingFile),
    typography: extractTypography(typographyFile),
    source: "design-system",
    extractedAt: new Date().toISOString()
  };

  if (config.mcp && config.mcp.enabled) {
    const mcpFile = path.resolve(repoRoot, config.mcp.tokensFile);
    if (fs.existsSync(mcpFile)) {
      try {
        const mcpTokens = JSON.parse(fs.readFileSync(mcpFile, "utf8"));
        tokens.mcp = mcpTokens;
      } catch (error) {
        log(`MCP token file exists but could not be parsed: ${error.message}`);
      }
    }
  }

  return tokens;
}

function renderPropsTable(parameters) {
  if (!parameters.length) {
    return "No parameters.";
  }

  const header = "| Name | Type | Default |\n|---|---|---|";
  const rows = parameters.map((param) => `| ${param.name} | ${param.type || "-"} | ${param.defaultValue || "-"} |`);
  return [header, ...rows].join("\n");
}

function generateComponentDoc(component) {
  const stateLine = component.stateParameters.length
    ? component.stateParameters.map((param) => `\`${param.name}: ${param.type}\``).join(", ")
    : "None";

  return [
    `# ${component.name}`,
    "",
    `Source: \`${component.path}\``,
    "",
    "## Description",
    "",
    `${component.name} is a Compose API extracted by the docs agent.`,
    "",
    "## Props",
    "",
    renderPropsTable(component.parameters),
    "",
    "## State Inputs",
    "",
    stateLine,
    "",
    "## Usage",
    "",
    "```kotlin",
    `${component.name}(`,
    "    // TODO: provide arguments",
    ")",
    "```",
    "",
    "## Previews",
    "",
    component.previews.length ? component.previews.map((item) => `- ${item}`).join("\n") : "No previews found."
  ].join("\n");
}

function generateDocs(config, components, tokens) {
  const docsRoot = path.resolve(repoRoot, config.docsPath);
  const componentsDir = path.join(docsRoot, "components");
  const tokensDir = path.join(docsRoot, "tokens");
  const designSystemDir = path.join(docsRoot, "design-system");
  const architectureDir = path.join(docsRoot, "architecture");
  const generatedDir = path.join(docsRoot, ".generated");

  [componentsDir, tokensDir, designSystemDir, architectureDir, generatedDir].forEach(ensureDir);

  for (const component of components) {
    writeText(path.join(componentsDir, `${component.name}.md`), generateComponentDoc(component));
  }

  const componentsIndex = [
    "# Components",
    "",
    `Generated at ${new Date().toISOString()}`,
    "",
    ...components.map((component) => `- [${component.name}](./${component.name}.md)`)
  ].join("\n");
  writeText(path.join(componentsDir, "index.md"), componentsIndex);

  const colorRows = (items) => items.map((item) => `| ${item.name} | ${item.hex} |`).join("\n");
  const colorsDoc = [
    "# Color Tokens",
    "",
    "## Light",
    "",
    "| Name | Hex |",
    "|---|---|",
    colorRows(tokens.colors.light),
    "",
    "## Dark",
    "",
    "| Name | Hex |",
    "|---|---|",
    colorRows(tokens.colors.dark)
  ].join("\n");
  writeText(path.join(tokensDir, "colors.md"), colorsDoc);

  const spacingDoc = [
    "# Spacing Tokens",
    "",
    "| Name | Value |",
    "|---|---|",
    ...tokens.spacing.map((item) => `| ${item.name} | ${item.value} |`)
  ].join("\n");
  writeText(path.join(tokensDir, "spacing.md"), spacingDoc);

  const typographyDoc = [
    "# Typography Tokens",
    "",
    "| Name | Font Size | Line Height | Font Weight | Letter Spacing |",
    "|---|---|---|---|---|",
    ...tokens.typography.map((item) => `| ${item.name} | ${item.fontSize} | ${item.lineHeight} | ${item.fontWeight} | ${item.letterSpacing} |`)
  ].join("\n");
  writeText(path.join(tokensDir, "typography.md"), typographyDoc);

  const dsOverview = [
    "# Design System Docs",
    "",
    "This folder is generated by the docs-agent pipeline.",
    "",
    "- [Components](../components/index.md)",
    "- [Color Tokens](../tokens/colors.md)",
    "- [Spacing Tokens](../tokens/spacing.md)",
    "- [Typography Tokens](../tokens/typography.md)"
  ].join("\n");
  writeText(path.join(designSystemDir, "overview.md"), dsOverview);

  const architectureDoc = [
    "# Documentation Pipeline",
    "",
    "1. Scan project",
    "2. Extract Kotlin components",
    "3. Extract design tokens",
    "4. Generate docs",
    "5. Organize docs",
    "6. Build Docusaurus site"
  ].join("\n");
  writeText(path.join(architectureDir, "documentation-pipeline.md"), architectureDoc);

  writeText(path.join(generatedDir, "components.json"), JSON.stringify(components, null, 2));
  writeText(path.join(generatedDir, "tokens.json"), JSON.stringify(tokens, null, 2));
}

function runCommand(command, args, workingDirectory = repoRoot) {
  const isWindowsNpm = process.platform === "win32" && command === "npm";
  const result = isWindowsNpm
    ? spawnSync("cmd", ["/c", "npm", ...args], { cwd: workingDirectory, encoding: "utf8" })
    : spawnSync(command, args, { cwd: workingDirectory, encoding: "utf8" });
  return {
    status: typeof result.status === "number" ? result.status : 1,
    stdout: result.stdout || "",
    stderr: result.error ? result.error.message : (result.stderr || "")
  };
}

function buildDocsSite(config, allowPublishFailure) {
  if (!config.autoPublish || !config.docusaurus || !config.docusaurus.enabled) {
    log("Docusaurus build disabled by config.");
    return { built: false, skipped: true };
  }

  const command = config.docusaurus.command || "npm";
  const args = Array.isArray(config.docusaurus.args) ? config.docusaurus.args : ["run", "build"];
  const cwd = path.resolve(repoRoot, config.docusaurus.cwd || "website");
  const result = runCommand(command, args, cwd);

  if (result.stdout.trim()) {
    console.log(result.stdout.trim());
  }

  if (result.status !== 0) {
    const errorMessage = result.stderr.trim() || "Unknown Docusaurus build error.";
    if (allowPublishFailure) {
      log(`Docusaurus build failed but allowed to continue: ${errorMessage}`);
      return { built: false, skipped: false, error: errorMessage };
    }

    throw new Error(`Docusaurus build failed: ${errorMessage}`);
  }

  log("Docusaurus build completed.");
  return { built: true, skipped: false };
}

function main() {
  const args = parseArgs(process.argv.slice(2));
  const config = readJson(args.config, {});

  const cacheFile = path.resolve(repoRoot, config.cache.file);
  const trackedFiles = getTrackedKotlinFiles();
  const snapshot = calculateSnapshot(trackedFiles);
  const changedFiles = getChangedFiles(args.from, args.to);
  const previousCache = readJson(cacheFile, { snapshot: "" });

  const relevantChanges = hasRelevantChanges(changedFiles);
  const shouldGenerate = relevantChanges || previousCache.snapshot !== snapshot;

  if (!shouldGenerate) {
    log("No relevant Kotlin/design-system/token changes detected. Skipping docs generation.");
    if (args.mode === "publish" || args.mode === "run") {
      buildDocsSite(config, args.allowPublishFailure);
    }
    return;
  }

  log("Starting docs generation pipeline.");
  const components = extractKotlinComponents();
  const tokens = extractDesignTokens(config);
  generateDocs(config, components, tokens);

  ensureDir(path.dirname(cacheFile));
  writeText(cacheFile, JSON.stringify({
    snapshot,
    generatedAt: new Date().toISOString(),
    componentCount: components.length,
    changedFiles
  }, null, 2));

  log(`Generated docs for ${components.length} components.`);

  if (args.mode === "publish" || args.mode === "run") {
    buildDocsSite(config, args.allowPublishFailure);
  }
}

try {
  main();
} catch (error) {
  console.error(`[docs-agent] ${error.message}`);
  process.exit(1);
}

