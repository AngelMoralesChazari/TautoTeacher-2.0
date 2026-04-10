param(
    [string] $JdkHome = $env:JAVA_HOME
)

$ErrorActionPreference = "Stop"
if (-not $JdkHome -or -not (Test-Path "$JdkHome\bin\javac.exe")) {
    Write-Error "No se encontro javac. Instala JDK 25 y define JAVA_HOME, o usa: .\compile.ps1 -JdkHome 'C:\Program Files\Java\jdk-25'"
}

$javac = Join-Path $JdkHome "bin\javac.exe"
$java = Join-Path $JdkHome "bin\java.exe"

$root = $PSScriptRoot
$srcJava = Join-Path $root "src\main\java"
$files = @()
if (Test-Path $srcJava) {
    $files = Get-ChildItem -Path $srcJava -Recurse -Filter "*.java" -File | ForEach-Object { $_.FullName } | Sort-Object -Unique
}

if ($files.Count -eq 0) {
    Write-Error "No se encontraron fuentes en $root"
}

Push-Location $root
try {
    & $javac --release 17 -encoding UTF-8 -d out @files
    $resSrc = Join-Path $root "src\main\resources"
    if (Test-Path $resSrc) {
        Copy-Item -Path $resSrc -Destination (Join-Path $root "out") -Recurse -Force
        Write-Host "Recursos copiados a out\"
    }
    Write-Host "OK: compilado con --release 17 en $root\out"
    Write-Host "Ejecutar app: & `"$java`" -cp out tautoteacher2.Main"
    Write-Host "Demo LogicScript: & `"$java`" -cp out tautoteacher2.logicscript.LogicScriptCli `"si llueve entonces llevo paraguas`""
    Write-Host "Regresion LogicScript: & `"$java`" -cp out tautoteacher2.logicscript.LogicScriptRegressionHarness"
}
finally {
    Pop-Location
}
