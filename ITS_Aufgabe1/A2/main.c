#include <stdio.h> 
#include <stdlib.h>



void exploit(char *InputString) { 
	char buf1[5]; 
	char buf2[5]; 
	 
	strcpy(buf1,"AAA"); /* Initialisierung mit Konstante */ 
	strcpy(buf2,"BBB"); /* Initialisierung mit Konstante */ 

	printf("\n\nVORHER:\n----------\n");
	printf("buf1: %s\n",buf1);
	printf("buf2: %s\n\n",buf2);

	//strcpy(buf1,InputString); /* Initialisierung mit Konstante */ 
	strcpy(buf2,InputString); /* Initialisierung mit Konstante */ 

	printf("\n\nNACHHER:\n----------\n");
	printf("buf1: %s\n",buf1);
	printf("buf2: %s\n\n",buf2);
} 

int main(int argc, char *argv[])
{
	char Eingabe[100];

	printf("Bitte einen Text eingeben:\n");
	fgets(Eingabe,100,stdin);

	exploit(Eingabe);

	return 0;
}


