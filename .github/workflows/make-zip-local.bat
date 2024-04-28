@echo off
setlocal enabledelayedexpansion

::     "version": { "major":"0", "minor": "2", "patch": "9f" },, "minor": "2", "patch": "9f" },
:: Set the input and 7zip directories
set "sevenZip=C:\Program Files\7-Zip\7z.exe"
set "sourceDir=D:\Games\Space-4x\Starsector-mod-dev\mods\Console Galaxy"
set "modsDirectory=D:\Games\Space-4x\Starsector\mods"

:: get the name of the mod from the sourceDir
:: for %%i in ("!sourceDir!") do set "modName=%%~nxi"

set "mod_info-filePath=!sourceDir!\mod_info.json"
:: if the above file is not fount prompt user and exit the script with any key
if not exist "%mod_info-filePath%" (
    echo File not found: %mod_info-filePath%
    pause
    exit /b
)

:: there are multiple lines in mod_info-filePath with the format "name": "*.*", save the first line to a variable
for /f "delims=" %%i in ('findstr /C:"name" "%mod_info-filePath%"') do (
    set "nameLine=%%i"
    goto :break
)
:break
:: Extract everything after ["name":] and save it to a variable "modFolderName"
for /f "tokens=2 delims=:," %%i in ("!nameLine!") do set "modFolderName=%%i"
:: remove the quotes from the variable
set "modFolderName=!modFolderName:"=!"

REM set "modFolderName=!modFolderName:~1,-1!"

:: remove the first and last space from the variable, do not delete the spaces in the middle or any characters
for /f "tokens=*" %%i in ("!modFolderName!") do set "modFolderName=%%i"

:: echo Mod folder name:!modFolderName!

:: Search for the string and save it to a variable
for /f "delims=" %%i in ('findstr /C:"version" "%mod_info-filePath%"') do set "versionLine=%%i"
:: Display the result
:: echo !versionLine!

:: Extract the version number from the string and save it to separate variables
:: tokens define the parts of the string to extract
for /f "tokens=4,6,8 delims=:, " %%i in ("!versionLine!") do (
    set "major=%%i"
    set "minor=%%j"
    set "patch=%%k"
)
:: remove the quotes from the variables
set "vMajor=!major:"=!"
set "vMinor=!minor:"=!"
set "vPatch=!patch:"=!"
:: set a variable to the version number with the format "Explore the Galaxy-major.minor.patch"
set "versionFormat=!modFolderName!-!vMajor!.!vMinor!.!vPatch!"
:: display mod info taken from the mod_info-filePath file
echo.
echo    Mod Name: "!modFolderName!"
echo    Version:
echo    Major: "!vMajor!" Minor: "!vMinor!" Patch: "!vPatch!"
echo    Mod Folder will be created as: "!versionFormat!"

:: Set the output and extract directories using the modName
set "outputDir=%sourceDir%\out\zip-temp_delete_if_too_large\!versionFormat!.zip"
:: set outputDirVar to the outputDir without the !versionFormat!.zip
set "outputDirVar=%sourceDir%\out\zip-temp_delete_if_too_large"
set "extractDir=%modsDirectory%\!versionFormat!"


:: print multiple lines of text to the console\
echo _______________________________________________________________
echo    Source Directory: 
echo    "%sourceDir%"
echo _______________________________________________________________
echo.
echo    This will create the zip file in a temporary folder: 
echo    "%outputDir%\%versionFormat%.zip"
echo    this temporary folder will be deleted after extraction
echo _______________________________________________________________
echo.
echo    zip file will be extracted to the mods folder: 
echo    "%extractDir% "
echo    with the folder name "%versionFormat%"
echo _______________________________________________________________
echo.
echo    any folder with the name : "%versionFormat%" or matching "!modFolderName!-*.*.*" will be deleted
echo _______________________________________________________________
echo.
set /p userInput=" Press Enter to continue..."

:: delete the extractDir if it exists
if exist "%extractDir%" rd /s /q "%extractDir%"
:: delete any folder in "modsDirectory" that matches "modFolderName-*.*.*"
for /d %%i in ("%modsDirectory%\!modFolderName!*") do rd /s /q "%%i"

:: use the 7zip command to create a zip file from the source directory, files and folders are defined below:
"%sevenZip%" a -tzip -bb1 "%outputDir%" "%sourceDir%\data" "%sourceDir%\graphics" "%sourceDir%\jars" "%sourceDir%\sounds" "%sourceDir%\src" "%sourceDir%\mod_pictures" "%sourceDir%\consolegalaxy.version" "%sourceDir%\mod_info.json" "%sourceDir%\icon.png"
"%sevenZip%" x -bb0 "%outputDir%" -o"%extractDir%"
:: set the size of the zip file and the extracted directory
for %%A in ("%outputDir%") do set "size=%%~zA"
set /a "sizeMB=%size% / 1024 / 1024"
set /a "sizeRemainder=%size% %% (1024 * 1024) * 1000 / (1024 * 1024)"
echo Size of the zip file is %sizeMB%.%sizeRemainder% MB

:: set the size of the extracted directory and display it
for /r "%extractDir%" %%A in (*) do set /a "dirSize+=%%~zA"
set /a "dirSizeMB=%dirSize% / 1024 / 1024"
set /a "dirSizeRemainder=%dirSize% %% (1024 * 1024) * 1000 / (1024 * 1024)"
echo Size of the extracted directory is %dirSizeMB%.%dirSizeRemainder% MB

:: delete the "outputDirVar" if it exists
@REM if exist "%outputDirVar%" rd /s /q "%outputDirVar%"

:: Initialize a counter
set /a "count=0"

:: Loop over the .zip files in reverse date order
for /f "delims=" %%A in ('dir /b /o-d /a-d "%outputDirVar%\*.zip"') do (
    :: Increment the counter
    set /a "count+=1"

    :: Skip the first file (the latest .zip file)
    if !count! gtr 1 (
        :: Delete the file
        del "%outputDirVar%\%%A"
    )
)

endlocal
pause
