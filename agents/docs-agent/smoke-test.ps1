[CmdletBinding()]
param()

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..\..")
$runnerPath = Join-Path $repoRoot "agents\docs-agent\runner.js"

node $runnerPath --mode generate
node $runnerPath --mode publish --allow-publish-failure

Write-Host "Docs agent smoke test completed."

