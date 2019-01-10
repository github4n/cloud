#!/bin/bash

errexit(){
	echo err: $1
	exit 1
}

# -n 无法正确识别传入参数空，使用! -z
#certificate password
test ! -z $1 && password=$1 || errexit "password empty"

#证书 CN 
test ! -z $2 && clientCN=$2 || errexit "clientCN empty"

#证书文件名
test ! -z $3 && client=$3 || errexit "client name empty"

#证书有效期 
test ! -z $4 && validity=$4 || errexit "cert validity empty"

#证书生成目录
test ! -z $5 && generateFilePath=$5 || generateFilePath=$(pwd)

#根证书名称
test ! -z $6 && CA=$6 || CA="LeedarsonRootCAX"

#生成证书的后缀
test ! -z $7 && crtsurfix=$7 || crtsurfix="crt"

#生成证书秘钥的后缀
test ! -z $8 && keysurfix=$8 || keysurfix="key"

test $crtsurfix = $keysurfix && errexit "crtsurfix equal keysurfix is forbid"

tmpdir="/tmp/lds-crt/$client"

mkdir -p $tmpdir
test $? -ne 0 && errexit "mkdir $tmpdir failed"

openssl genrsa -out $tmpdir/$client.$keysurfix 2048
test $? -ne 0 && errexit "gen $tmpdir/$client.$keysurfix failed"

openssl req -new -subj /C=CN/ST=ShenZhen/O=Leedarson\ Lighting\ Co.\ Ltd./CN=$clientCN -key $tmpdir/$client.$keysurfix -out $tmpdir/$client.csr
test $? -ne 0 && errexit "gen $tmpdir/$client.csr failed"

openssl x509 -req -days $validity -in $tmpdir/$client.csr -CA $CA.crt -CAkey $CA.key -set_serial 01 -out $tmpdir/$client.$crtsurfix
test $? -ne 0 && errexit "gen $tmpdir/$client.$crtsurfix failed"

\cp -f $tmpdir/${client}.$keysurfix $tmpdir/${client}.$crtsurfix $generateFilePath/
test $? -ne 0 && errexit "cp -f $tmpdir/${client}.$keysurfix $tmpdir/${client}.$crtsurfix $generateFilePath/ failed"

# test 也会返回1， 需主动返回0
exit 0



