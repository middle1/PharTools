<?php
ini_set('phar.readonly', 0);

$sourceFolder = $argv[1];
$outPharNameAndPath = $argv[2];
$stub = $argv[3];

$folderName = basename($sourceFolder);

$pharName = $outPharNameAndPath ."/". $folderName.".phar";

try{

$phar = new Phar($pharName);
$phar->setStub('<?php '.$stub.' ?>');
$phar->buildFromDirectory($sourceFolder);

}catch(Exception $ex){

echo $ex->getMessage();
}