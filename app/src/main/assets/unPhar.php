<?php
if(isset($argv[1]) && isset($argv[1])) {
    $pharFile = $argv[1]; //input
    $pathToExtract = $argv[2]; // output
    $pharName = pathinfo($pharFile, PATHINFO_FILENAME);

// Создаем папку с именем архива
$extractPath = $pathToExtract . '/' . $pharName;
mkdir($extractPath);

// Извлекаем файлы в созданную папку
$phar = new Phar($pharFile);
try{
$phar->extractTo($extractPath);
}catch(Excaption $e){
}
} else {
    echo "Error";
}