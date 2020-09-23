#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <iostream>


char* UNIXconversion(char *cmd) {
    char *UNIXcommand = malloc(6 * sizeof(char));

    if (strcmp(cmd, "cd") == 0)
    {
        UNIXcommand = "cd";
        return UNIXcommand;
    }
    else if (strcmp(cmd, "dir") == 0)
    {
        UNIXcommand = "ls";
        return UNIXcommand;
    }
    else if (strcmp(cmd, "type") == 0)
    {
        UNIXcommand = "cat";
        return UNIXcommand;
    }
    else if (strcmp(cmd, "del") == 0)
    {
        UNIXcommand = "rm";
        return UNIXcommand;
    }
    else if (strcmp(cmd, "ren") == 0)
    {
        UNIXcommand = "mv";
        return UNIXcommand;
    }
    else if (strcmp(cmd, "copy") == 0)
    {
        UNIXcommand = "cp";
        return UNIXcommand;
    }
    else
    {
        return NULL;
    }
    
}

int commandEnter(char *cmd, char *arg1, char *arg2)
{
    if (strcmp(cmd, "cd") == 0 && sizeof(arg1) > 0 && sizeof(arg2) == 0)
    {
        chdir
    }
    else if (strcmp(cmd, "dir") == 0)
    {
        
    }
    else if (strcmp(cmd, "type") == 0)
    {
        
    }
    else if (strcmp(cmd, "del") == 0)
    {
        
    }
    else if (strcmp(cmd, "ren") == 0)
    {
        
    }
    else if (strcmp(cmd, "copy") == 0)
    {
        
    }
    return 1;
}

int main()
{
    int index, cmdLength, arg1Length, arg2Length;
    char line[32]; // size is prob bigger than needed tbh
    printf("Enter DOS commands:\t");
    // reads characters from user input (stdin) and stores them in the character array called line
    while (fgets(line, 32, stdin) == NULL)
    {
        char *command = malloc(6 * sizeof(char)); // allocate memory for a string of 6 characters (i chose 6 just because its large enough to fit all of the commands he gave us
        char *arg1 = malloc(20 * sizeof(char)); // allocate memory for the first argument (no idea how long this will be, could experiment with different sizes later
        char *arg2 = malloc(20 * sizeof(char)); // allocate memory for the second argument (no idea how long this will be, could experiment with different sizes later


        const char* wsp = " \t"; // will be used to split the input from the first command to the first occurrence of a space or tab character

        cmdLength = strcspn(line, wsp); // counts the number of contiguous characters that are NOT whitespace characters (stops when it reaches the first occurrence of a space or tab char

        strncpy(command, line, cmdLength); // copy into the string 'command' characters from 'line' starting at index 0 and continuing until 'cmdLength' characters have been read

        index += cmdLength + 1;

        arg1Length = strcspn(line + index, wsp);
        strncpy(arg1, line + index, arg1Length); 
        
        index += arg1Length + 1;

        arg2Length = strcspn(line + index, wsp);
        strncpy(arg2, line + index, arg1Length); 

        printf("DOS: command: %s\tlength = %d\n\targ1: %s\tlength = %d\n\targ2: %s\tlength = %d\n", 
        command, (int)strlen(command), arg1, (int)strlen(arg1), arg2, (int)strlen(arg2));

        char *UNIXcommand = malloc(6 * sizeof(char));
        char *UNIXarg1 = malloc(20 * sizeof(char));
        char *UNIXarg2 = malloc(20 * sizeof(char));

        UNIXcommand = UNIXconversion(command);
        if (UNIXcommand != NULL)
        {
            
        }
        else
        {
            printf("ERROR: DOS command not recognized\n");
        }

        
    }

    
    return 0;
}