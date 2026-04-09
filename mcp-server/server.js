const express = require("express");
const fs = require("fs");
const path = require("path");

const app = express();
const PORT = 3333;

const repoRoot = path.resolve(__dirname, "..");
const aiRoot = path.resolve(repoRoot, ".ai");
const agentsDir = path.join(aiRoot, "agents");
const skillsDir = path.join(aiRoot, "skills");

app.use(express.json());

app.use((req, res, next) => {
  const timestamp = new Date().toISOString();
  console.log(`[${timestamp}] ${req.method} ${req.originalUrl}`);
  next();
});

function readFileIfExists(filePath) {
  if (!fs.existsSync(filePath)) {
    return null;
  }
  return fs.readFileSync(filePath, "utf8");
}

function getAgentFiles() {
  if (!fs.existsSync(agentsDir)) {
    return [];
  }

  return fs
    .readdirSync(agentsDir)
    .filter((name) => name.toLowerCase().endsWith(".json"))
    .sort();
}

function loadAgentFromFile(fileName) {
  const filePath = path.join(agentsDir, fileName);
  const raw = readFileIfExists(filePath);

  if (!raw) {
    return null;
  }

  try {
    const parsed = JSON.parse(raw);
    if (!parsed.name || !parsed.description || !parsed.prompt) {
      return null;
    }

    return {
      ...parsed,
      sourceFile: fileName
    };
  } catch (error) {
    console.warn(`Invalid agent JSON: ${fileName}`, error.message);
    return null;
  }
}

function loadAllAgents() {
  const files = getAgentFiles();
  return files
    .map((fileName) => loadAgentFromFile(fileName))
    .filter((agent) => agent !== null);
}

function resolveSkillFile(agentSkill) {
  if (!agentSkill) {
    return null;
  }

  const normalized = agentSkill.endsWith(".md") || agentSkill.endsWith(".txt")
    ? agentSkill
    : `${agentSkill}.md`;
  const skillPath = path.join(skillsDir, normalized);

  if (!fs.existsSync(skillPath)) {
    return null;
  }

  return {
    name: normalized,
    content: fs.readFileSync(skillPath, "utf8")
  };
}

function getProjectContext() {
  const settingsPath = path.join(repoRoot, "settings.gradle.kts");
  const appBuildPath = path.join(repoRoot, "app", "build.gradle.kts");
  const readmePath = path.join(repoRoot, "README.md");

  const settings = readFileIfExists(settingsPath) || "";
  const modules = Array.from(settings.matchAll(/include\("([^"]+)"\)/g)).map((match) => match[1]);

  const context = {
    root: path.basename(repoRoot),
    modules,
    hasAppModule: fs.existsSync(path.join(repoRoot, "app")),
    hasReadme: fs.existsSync(readmePath),
    hasAppBuildFile: fs.existsSync(appBuildPath)
  };

  return context;
}

function composePrompt({ agent, userInput, skillContent, projectContext }) {
  const sections = [];

  sections.push("# Agent");
  sections.push(agent.name);
  sections.push("");
  sections.push("# Agent Prompt");
  sections.push(agent.prompt);

  if (skillContent) {
    sections.push("");
    sections.push("# Skill");
    sections.push(skillContent);
  }

  sections.push("");
  sections.push("# User Input");
  sections.push(userInput);

  if (projectContext) {
    sections.push("");
    sections.push("# Project Context");
    sections.push(JSON.stringify(projectContext, null, 2));
  }

  return sections.join("\n");
}

app.get("/health", (req, res) => {
  res.json({
    status: "ok",
    server: "androidcore-local-mcp",
    port: PORT,
    aiRoot,
    timestamp: new Date().toISOString()
  });
});

app.get("/agents", (req, res) => {
  const agents = loadAllAgents().map((agent) => ({
    name: agent.name,
    version: agent.version || "1.0.0",
    description: agent.description,
    skill: agent.skill || null,
    sourceFile: agent.sourceFile
  }));

  res.json({
    total: agents.length,
    agents
  });
});

app.get("/agents/:name", (req, res) => {
  const targetName = req.params.name.toLowerCase();
  const agent = loadAllAgents().find((item) => item.name.toLowerCase() === targetName);

  if (!agent) {
    res.status(404).json({
      error: `Agent '${req.params.name}' was not found in ${agentsDir}`
    });
    return;
  }

  const skill = resolveSkillFile(agent.skill);
  res.json({
    ...agent,
    skillContent: skill ? skill.content : null
  });
});

app.post("/generate", (req, res) => {
  const { agentName, userInput, includeProjectContext = true } = req.body || {};

  if (!agentName || !userInput) {
    res.status(400).json({
      error: "Request body must include 'agentName' and 'userInput'."
    });
    return;
  }

  const agent = loadAllAgents().find((item) => item.name.toLowerCase() === String(agentName).toLowerCase());
  if (!agent) {
    res.status(404).json({
      error: `Agent '${agentName}' was not found in ${agentsDir}`
    });
    return;
  }

  const skill = resolveSkillFile(agent.skill);
  const prompt = composePrompt({
    agent,
    userInput,
    skillContent: skill ? skill.content : null,
    projectContext: includeProjectContext ? getProjectContext() : null
  });

  res.json({
    agent: {
      name: agent.name,
      version: agent.version || "1.0.0",
      skill: skill ? skill.name : null
    },
    generatedPrompt: prompt,
    metadata: {
      localOnly: true,
      includeProjectContext,
      timestamp: new Date().toISOString()
    }
  });
});

app.listen(PORT, "127.0.0.1", () => {
  console.log(`AndroidCore local MCP server running at http://127.0.0.1:${PORT}`);
  console.log(`Reading agents from: ${agentsDir}`);
  console.log(`Reading skills from: ${skillsDir}`);
});

