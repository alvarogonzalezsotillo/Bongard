@echo off

echo " " > bongard.urls

set URLBASE=http://www.foundalis.com/res/bps/bongard/
for /L %%i in (1, 1, 9) do echo %URLBASE%p00%%i.gif >> bongard.urls
for /L %%i in (10, 1, 99) do echo %URLBASE%p0%%i.gif >> bongard.urls
echo %URLBASE%p100.gif >> bongard.urls


set URLBASE=http://www.foundalis.com/res/bps/doughof/
for /L %%i in (100, 1, 156) do echo %URLBASE%p%%i.gif >> bongard.urls


set URLBASE=http://www.foundalis.com/res/bps/foundal/
for /L %%i in (157, 1, 200) do echo %URLBASE%p%%i.gif >> bongard.urls


set URLBASE=http://www.foundalis.com/res/bps/insana/
for /L %%i in (201, 1, 232) do echo %URLBASE%p%%i.gif >> bongard.urls


set URLBASE=http://www.foundalis.com/res/bps/shanahan/
for /L %%i in (233, 1, 237) do echo %URLBASE%p%%i.gif >> bongard.urls


set URLBASE=http://www.foundalis.com/res/bps/foundal/
for /L %%i in (238, 1, 239) do echo %URLBASE%p%%i.gif >> bongard.urls

set URLBASE=http://www.foundalis.com/res/bps/howells/
for /L %%i in (240, 1, 244) do echo %URLBASE%p%%i.gif >> bongard.urls

set URLBASE=http://www.foundalis.com/res/bps/gunnarsson/
for /L %%i in (245, 1, 260) do echo %URLBASE%p%%i.gif >> bongard.urls

set URLBASE=http://www.foundalis.com/res/bps/ihde/
for /L %%i in (261, 1, 261) do echo %URLBASE%p%%i.gif >> bongard.urls

set URLBASE=http://www.foundalis.com/res/bps/barenbaum/
for /L %%i in (262, 1, 263) do echo %URLBASE%p%%i.gif >> bongard.urls

set URLBASE=http://www.foundalis.com/res/bps/merse/
for /L %%i in (264, 1, 280) do echo %URLBASE%p%%i.gif >> bongard.urls




wget -c -i bongard.urls -nv