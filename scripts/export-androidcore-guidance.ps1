[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$TargetRepoPath,

    [string]$ConfigPath = (Join-Path $PSScriptRoot "export-androidcore-guidance.config.json"),

    [switch]$Force
)

$ErrorActionPreference = "Stop"

function Resolve-FullPath {
    param(
        [Parameter(Mandatory = $true)]
        [string]$BasePath,
        [Parameter(Mandatory = $true)]
        [string]$ChildPath
    )

    return [System.IO.Path]::GetFullPath((Join-Path $BasePath $ChildPath))
}

function Ensure-ParentDirectory {
    param(
        [Parameter(Mandatory = $true)]
        [string]$FilePath
    )

    $directory = Split-Path -Parent $FilePath
    if (-not [string]::IsNullOrWhiteSpace($directory) -and -not (Test-Path -LiteralPath $directory)) {
        New-Item -ItemType Directory -Path $directory -Force | Out-Null
    }
}

function Write-TextFile {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path,
        [Parameter(Mandatory = $true)]
        [string]$Content,
        [switch]$ForceWrite
    )

    Ensure-ParentDirectory -FilePath $Path

    if ((Test-Path -LiteralPath $Path) -and -not $ForceWrite) {
        throw "Target file already exists: $Path. Re-run with -Force to overwrite."
    }

    [System.IO.File]::WriteAllText($Path, $Content, [System.Text.UTF8Encoding]::new($false))
}

$repoRoot = Resolve-FullPath -BasePath $PSScriptRoot -ChildPath ".."
$targetRoot = [System.IO.Path]::GetFullPath($TargetRepoPath)
$configFullPath = if ([System.IO.Path]::IsPathRooted($ConfigPath)) { $ConfigPath } else { Resolve-FullPath -BasePath $PWD.Path -ChildPath $ConfigPath }

if (-not (Test-Path -LiteralPath $targetRoot)) {
    throw "Target repository path does not exist: $targetRoot"
}

if (-not (Test-Path -LiteralPath $configFullPath)) {
    throw "Config file not found: $configFullPath"
}

$config = Get-Content -LiteralPath $configFullPath -Raw | ConvertFrom-Json
$utcNow = (Get-Date).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")

foreach ($mapping in $config.templateMappings) {
    $sourcePath = Resolve-FullPath -BasePath $repoRoot -ChildPath $mapping.source
    $targetPath = Resolve-FullPath -BasePath $targetRoot -ChildPath $mapping.target

    if (-not (Test-Path -LiteralPath $sourcePath)) {
        throw "Template source file not found: $sourcePath"
    }

    $content = Get-Content -LiteralPath $sourcePath -Raw
    $header = @(
        "<!-- Generated from AndroidCore -->",
        "<!-- Source: $($mapping.source) -->",
        "<!-- Synced at (UTC): $utcNow -->",
        ""
    ) -join [Environment]::NewLine

    Write-TextFile -Path $targetPath -Content ($header + $content) -ForceWrite:$Force
    Write-Host "Exported template: $targetPath"
}

$bundleTargetPath = Resolve-FullPath -BasePath $targetRoot -ChildPath $config.bundle.target
$bundleParts = New-Object System.Collections.Generic.List[string]
$bundleParts.Add("# AndroidCore Guidance Bundle")
$bundleParts.Add("")
$bundleParts.Add("This file was generated from the AndroidCore repository.")
$bundleParts.Add("It exists inside the consumer repository so Copilot can read the guidance locally.")
$bundleParts.Add("")
$bundleParts.Add("- Source repository: $repoRoot")
$bundleParts.Add("- Synced at (UTC): $utcNow")

foreach ($source in $config.bundle.sources) {
    $sourcePath = Resolve-FullPath -BasePath $repoRoot -ChildPath $source
    if (-not (Test-Path -LiteralPath $sourcePath)) {
        throw "Bundle source file not found: $sourcePath"
    }

    $bundleParts.Add("")
    $bundleParts.Add("---")
    $bundleParts.Add("")
    $bundleParts.Add("## Source: $source")
    $bundleParts.Add("")
    $bundleParts.Add((Get-Content -LiteralPath $sourcePath -Raw).TrimEnd())
}

$bundleContent = ($bundleParts -join [Environment]::NewLine) + [Environment]::NewLine
Write-TextFile -Path $bundleTargetPath -Content $bundleContent -ForceWrite:$Force
Write-Host "Exported guidance bundle: $bundleTargetPath"

Write-Host "AndroidCore guidance export completed successfully."

