#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>


int commandEnter(char *UNIXcommand, char *arg1, char *arg2)
{
    if (strcmp(UNIXcommand, "cd") == 0 && sizeof(arg1) > 0)
    {
        //printf("chdir(%s)\n", arg1);
        chdir(arg1);
    }
    else if (strlen(UNIXcommand) != 0 && sizeof(arg1) != 0 && sizeof(arg2) != 0)
    {
        strcat(UNIXcommand, " ");
        strcat(UNIXcommand, arg1);
        strcat(UNIXcommand, " ");
        strcat(UNIXcommand, arg2);
        system(UNIXcommand);
        return 0;
    }
    else if (strcmp(UNIXcommand, "ls") == 0)
    {
        system(UNIXcommand);
        return 0;
    }
    else if (strcmp(UNIXcommand, "cat") == 0) 
    {
        strcat(UNIXcommand, " ");
        strcat(UNIXcommand, arg1);
        system(UNIXcommand);
    }
    return 1;
}

int main()
{
    int index, cmdLength, arg1Length, arg2Length;
    char line[32]; // size is prob bigger than needed tbh
    // reads characters from user input (stdin) and stores them in the character array called line
    //printf("Type Ctrl-c to end command interpretation\n");
    printf("Enter DOS commands\n");
    
    index = 0;

    while (fgets(line, 100, stdin))
    {
        index = 0;
        
        //printf("Enter DOS commands:\n");
        // allocate memory for the character sequences
        char *command = malloc(50 * sizeof(char));
        char *arg1 = malloc(20 * sizeof(char));
        char *arg2 = malloc(20 * sizeof(char));

        int wspLength;


        const char* wsp = " \t\n\r"; // will be used to split the input from the first command to the first occurrence of a space or tab character

        cmdLength = strcspn(line, wsp); // counts the number of contiguous characters that are NOT whitespace characters (stops when it reaches the first occurrence of a space or tab char

        strncpy(command, line, cmdLength); // copy into the string 'command' characters from 'line' starting at index 0 and continuing until 'cmdLength' characters have been read
        //printf("cmd:\t%s\n",command);

        index += cmdLength;

        wspLength = strspn(line + index, wsp);
        index += wspLength;
        arg1Length = strcspn(line + index, wsp);
        strncpy(arg1, line + index, arg1Length); 
        //printf("arg1:\t%s\n", arg1);
        
        index += arg1Length;

        wspLength = strspn(line + index, wsp);
        index += wspLength;
        arg2Length = strcspn(line + index, wsp);
        strncpy(arg2, line + index, arg1Length); 
        //printf("arg2:\t%s\n", arg2);

        ////printf("\n\tDOS: command: %s\tlength = %d\n\targ1: %s\tlength = %d\n\targ2: %s\tlength = %d\n", command, (int)strlen(command), arg1, (int)strlen(arg1), arg2, (int)strlen(arg2));

        char *UNIXcommand = malloc(50 * sizeof(char));

        if (strcmp(command, "cd") == 0)
        {
            strcpy(UNIXcommand, "cd");
            //printf("UNIX:\t%s\n", UNIXcommand);
        }
        else if (strcmp(command, "dir") == 0)
        {
            strcpy(UNIXcommand, "ls");
            //printf("UNIX:\t%s\n", UNIXcommand);
        }
        else if (strcmp(command, "type") == 0)
        {
            strcpy(UNIXcommand, "cat");
            //printf("UNIX:\t%s\n", UNIXcommand);
        }
        else if (strcmp(command, "del") == 0)
        {
            strcpy(UNIXcommand, "rm");
            //printf("UNIX:\t%s\n", UNIXcommand);
        }
        else if (strcmp(command, "ren") == 0)
        {
            strcpy(UNIXcommand, "mv");
            //printf("UNIX:\t%s\n", UNIXcommand);
        }
        else if (strcmp(command, "copy") == 0)
        {
            strcpy(UNIXcommand, "cp");
            //printf("UNIX:\t%s\n", UNIXcommand);
        }
        else
        {
            //printf("ERROR: DOS command not recognized\n");
        }


        if (UNIXcommand != NULL)
        {
            commandEnter(UNIXcommand, arg1, arg2);
            //printf("\tUNIX command is now:\t%s\n", UNIXcommand);
        }
        else
        {
            //printf("ERROR: DOS command not recognized\n");
        }
        printf("Type Ctrl-c to end command interpretation\n");

        
    }

    
    return 0;
}
