[CmdletBinding()]
param()

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$templatePath = Join-Path $repoRoot "scripts\hooks\pre-push"
$gitHooksDir = Join-Path $repoRoot ".git\hooks"
$hookPath = Join-Path $gitHooksDir "pre-push"

if (-not (Test-Path -LiteralPath $templatePath)) {
    throw "Hook template not found: $templatePath"
}

if (-not (Test-Path -LiteralPath $gitHooksDir)) {
    throw "Git hooks directory not found: $gitHooksDir"
}

$content = Get-Content -LiteralPath $templatePath -Raw
[System.IO.File]::WriteAllText($hookPath, $content, [System.Text.UTF8Encoding]::new($false))
Write-Host "Installed docs pre-push hook: $hookPath"

