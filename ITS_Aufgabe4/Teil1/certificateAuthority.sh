#!/bin/bash

die(){
	echo "$1";
	exit 0;
}

[ ! -d "$PWD/certificateAuthority" ] && die "Run script from it's path";
[ ! -f "$PWD/certificateAuthority/openssl.cnf" ] && die "Missing openssl.cnf file";

PATH="$PWD/certificateAuthority";

echo "01" > $PATH/serial;
echo "" > $PATH/database.txt;

# 1.3
openssl req -new -x509 -days 3650 -config $PATH/openssl.cnf -keyform PEM -keyout $PATH/privateKey.pem -outform PEM -out $PATH/certificate.pem





#openssl x509 -in $PATH/certificate.pem -outform der -out $PATH/certificate.crt