package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CommandInvokerTest {

    private CommandInvoker commandInvoker;

    Logger logger;

    @BeforeEach
    public void setup(){
        logger = mock(Logger.class);
        commandInvoker = new CommandInvoker(logger);
    }

    @Test
    public void shouldExecuteCommand(){

        Command mockCommand = mock(Command.class);

        // Adiciona o comando à fila
        commandInvoker.addCommand(mockCommand);

        // Executa os comandos
        commandInvoker.executeCommands();

        verify(mockCommand, times(1)).execute();
    }

    @Test
    public void shouldExecuteMultipleCommands(){
        // Cria dois comandos mockados
        Command mockCommand1 = mock(Command.class);
        Command mockCommand2 = mock(Command.class);

        // Adiciona ambos à fila
        commandInvoker.addCommand(mockCommand1);
        commandInvoker.addCommand(mockCommand2);

        // Executa os comandos
        commandInvoker.executeCommands();

        verify(mockCommand1, times(1)).execute();
        verify(mockCommand2, times(1)).execute();
    }
}
