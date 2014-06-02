#!/bin/bash

die(){
	echo "$1";
	exit 0;
}

[ ! -d "$PWD/certified" ] && die "Run script from it's path";
[ "$1" == "" ] || [ "$2" == "" ] || [ "$3" == "" ] && die "Missing Parameter (Name Alias Password)";
[ -d "$PWD/certified/$1" ] && die "Certificate already exits";

NAME=$1;
ALIAS=$2;
PASS=$3;

PATH=certified/$NAME;
CA=certificateAuthority;

mkdir $PATH;

# 1.4
openssl genrsa -out $PATH/privateKey.pem 4096
openssl req -new -key $PATH/privateKey.pem -out $PATH/request.pem
openssl x509 -days 3650 -CA $CA/certificate.pem -CAkey $CA/privateKey.pem -req -in $PATH/request.pem -outform PEM -out $PATH/certificate.pem -CAserial $CA/serial

# 1.5
cat $PATH/privateKey.pem $PATH/certificate.pem > $PATH/privateKeyCertificate.pem
openssl pkcs12 -export -in $PATH/privateKeyCertificate.pem -out $PATH/certificate.pkcs12 -name $ALIAS -noiter -nomaciter
keytool -importkeystore -srckeystore $PATH/certificate.pkcs12 -srcstoretype pkcs12 -srcalias $ALIAS -destkeystore $PATH/certificate.jks -deststoretype jks -deststorepass $PASS -destalias $ALIAS