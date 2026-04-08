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
$dirs = @(
    "src\main\java\tautoteacher2",
    "src\main\java\tautoteacher2\ui",
    "src\main\java\tautoteacher2\servicio",
    "src\main\java\tautoteacher2\servicio\dto",
    "src\main\java\tautoteacher2\core\logica",
    "src\main\java\tautoteacher2\core\logica\parser",
    "src\main\java\tautoteacher2\core\visualizacion",
    "src\main\java\tautoteacher2\nlp\lexer",
    "src\main\java\tautoteacher2\nlp\parser",
    "src\main\java\tautoteacher2\nlp\lexicon",
    "src\main\java\tautoteacher2\nlp\semantica"
)

$files = @()
foreach ($rel in $dirs) {
    $dir = Join-Path $root $rel
    if (Test-Path $dir) {
        $files += Get-ChildItem -Path $dir -Filter "*.java" -File | ForEach-Object { $_.FullName }
    }
}
$files = $files | Sort-Object -Unique

if ($files.Count -eq 0) {
    Write-Error "No se encontraron fuentes en $root"
}

Push-Location $root
try {
    & $javac --release 25 -encoding UTF-8 -d out @files
    Write-Host "OK: compilado con --release 25 en $root\out"
    Write-Host "Ejecutar: & `"$java`" -cp out tautoteacher2.Main"
}
finally {
    Pop-Location
}
