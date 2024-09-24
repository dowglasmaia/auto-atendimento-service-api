package com.dowglasmaia.autoatendimentoserviceapi.domain.command;

import com.dowglasmaia.autoatendimentoserviceapi.infrastructure.logging.Logger;

import java.util.LinkedList;
import java.util.Queue;


public class CommandInvoker {

    private final Logger logger;

    public CommandInvoker(Logger logger){
        this.logger = logger;
    }

    private final Queue<Command> commandQueue = new LinkedList<>();

    public void addCommand(Command command){
        commandQueue.offer(command);
    }

    public void executeCommands(){
        while (!commandQueue.isEmpty()) {
            Command command = commandQueue.poll();
            logger.info("Executing command: " + command.getClass().getSimpleName());
            command.execute();
        }
    }
}
