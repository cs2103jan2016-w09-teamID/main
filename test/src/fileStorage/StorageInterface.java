package fileStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import entity.AllTaskLists;
import entity.TaskEntity;
import mainLogic.TaskManager;
import mainLogic.TaskManagerInterface;

public class StorageInterface {
    
    public StorageHandler storageHandler;
    public static final int QUEUE_SIZE = 5;
    
    public StorageInterface() {
        storageHandler = new StorageHandler();
    }
    
    public AllTaskLists getWorkingTaskLists () {
        TaskManager arrayDataLoader = TaskManager.getInstance();
        return arrayDataLoader.generateSavedTaskArray();
    }
    
    // Test function
    public static void main (String args[]) {
        StorageInterface sc = new StorageInterface();
        StorageHandler sh = new StorageHandler();
        JsonConverter jc = new JsonConverter();
        
        AllTaskLists dummyTL = sc.createDummy();
        sh.writeToMainFile(jc.javaToJson(dummyTL));
        
        //String data = fm.readFromExistingFile();
        //System.out.println(data);
        //AllTaskLists convertedDummy = jc.jsonToJava(data);
        
        Queue<String> dummyCommands = sc.createDummyCommands();
        sh.writeToCommandFile("HELLO COMMAND");
        //sh.writeToMainFile("HELLO MAIN");
        
        //ch.saveUponExit(true);
        //System.out.println(ch.readFromExistingCommandFile()); 
    }

    /**
     * Retrieve all tasks previously saved in the text file.
     * @return AllTaskLists
     */
    public AllTaskLists getTaskLists() {
        JsonConverter jsonConverter = new JsonConverter();
        
        String retrievedTasks = storageHandler.getAllStoredTasks();
        AllTaskLists retrievedList = jsonConverter.jsonToJava(retrievedTasks);
        
        return retrievedList;
    }

    /** 
     * Returns true if tasks are written to file, false otherwise. 
     * @return boolean
     */    
    public boolean storeTaskLists(AllTaskLists allTaskLists) {
        JsonConverter jsonConverter = new JsonConverter();
        
        String toStore = jsonConverter.javaToJson(allTaskLists);
        
        boolean isSaved = storageHandler.writeToMainFile(toStore);
        assert isSaved == true : "Tasks not stored.";
        
        return isSaved;
    }
    
    /** 
     * Returns true if tasks are written to file, false otherwise. 
     * @return boolean
     */
    public boolean storeTaskLists(ArrayList<TaskEntity> main, ArrayList<TaskEntity> floating){
        AllTaskLists newList = new AllTaskLists();
        
        newList.setFloatingTaskList(floating);
        newList.setMainTaskList(main);
        
        return storeTaskLists(newList);
    }
    
    public AllTaskLists getBackUpTaskLists() {
        JsonConverter jsonConverter = new JsonConverter();
        
        String retrievedTasks = storageHandler.getAllBackUpTasks();
        AllTaskLists retrievedList = jsonConverter.jsonToJava(retrievedTasks);
        
        return retrievedList;
    }
    
    public void deleteBackUp() {
        storageHandler.deleteBackUpFile();;
    }
        
    /**
     * Store input command into command file 
     * Re-writes main file if command queue is full
     * Current queue size is 5 for testing
     * @param command
     * @return isFullQueue
     */
    public boolean saveUponFullQueue(String command) {
        boolean isFullQueue = false;
        
        boolean isSaved = storageHandler.writeToCommandFile(command);
        assert isSaved == true : "Not commited to main file.";
      
        Queue<String> newCommandsQueue = getCommandsQueue();
        newCommandsQueue.offer(command);
        setCommandsQueue(newCommandsQueue);
        
        if (newCommandsQueue.size() >= QUEUE_SIZE) {
            isFullQueue = true;
        }
        return isFullQueue;
    }
    
    public Queue<String> getCommandsUponInit() {
        Queue<String> retrievedCommands = storageHandler.getAllCommandsQueue();
        
        return retrievedCommands;
    }
    
    public boolean storeCommandLine(String command) {
        return storageHandler.writeToCommandFile(command);
    }
        
    public Queue<String> getCommandsQueue() {
        return storageHandler.getAllCommandsQueue();
    }
    
    public void setCommandsQueue(Queue<String> newCommandsQueue) {
        storageHandler.setAllCommandsQueue(newCommandsQueue);
    }
    
    public void clearCommandFileOnCommit() {
        storageHandler.clearCommandFileUponCommit();
    }
    
    public void clearCommandFile() {
        storageHandler.clearCommandFile();
    }
    
    private AllTaskLists createDummy() {
        ArrayList<TaskEntity> dummyMainList = new ArrayList<TaskEntity>();
        dummyMainList.add(new TaskEntity("firstTask"));
        dummyMainList.add(new TaskEntity("secondTask"));
        assert dummyMainList.size() > 0;
        
        ArrayList<TaskEntity> dummyFloatingList = new ArrayList<TaskEntity>();
        dummyFloatingList.add(new TaskEntity("floatingTaskOne"));
        dummyFloatingList.add(new TaskEntity("floatingTaskTwo"));
        assert dummyFloatingList.size() > 0;
        
        AllTaskLists dummyTL = new AllTaskLists();
        dummyTL.setFloatingTaskList(dummyFloatingList);
        dummyTL.setMainTaskList(dummyMainList);
        return dummyTL;
    }
    
    private Queue<String> createDummyCommands() {
        Queue<String> dummyCommands = new LinkedList<String>();
        dummyCommands.offer("add whatever : blah blah at blah time blah data");
        dummyCommands.offer("add hello : blah blah at blah time blah data");
        dummyCommands.offer("add annyeong : blah blah at blah time blah data");
        return dummyCommands;
    }
}
