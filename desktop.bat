@echo off 
reg add "hkcu\control panel\desktop" /v wallpaper /d "%1" /f 
RunDll32.exe USER32.DLL,UpdatePerUserSystemParameters 
exit 