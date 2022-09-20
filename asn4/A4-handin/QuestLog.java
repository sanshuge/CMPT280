import com.opencsv.CSVReader;
import lib280.base.Pair280;
import lib280.hashtable.KeyedChainedHashTable280;
import lib280.tree.OrderedSimpleTree280;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

// This project uses a JAR called opencsv which is a library for reading and
// writing CSV (comma-separated value) files.
// 
// You don't need to do this for this project, because it's already done, but
// if you want to use opencsv in other projects on your own, here's the process:
//
// 1. Download opencsv-3.1.jar from http://sourceforge.net/projects/opencsv/
// 2. Drag opencsv-3.1.jar into your project.
// 3. Right-click on the project in the package explorer, select "Properties" (at bottom of popup menu)
// 4. Choose the "Libraries" tab
// 5. Click "Add JARs"
// 6. Select the opencsv-3.1.jar from within your project from the list.
// 7. At the top of your .java file add the following imports:
//        import java.io.FileReader;
//        import com.opencsv.CSVReader;
//
// Reference documentation for opencsv is here:  
// http://opencsv.sourceforge.net/apidocs/overview-summary.html


public class QuestLog extends KeyedChainedHashTable280<String, QuestLogEntry> {

	public QuestLog() {
		super();
	}
	
	/**
	 * Obtain an array of the keys (quest names) from the quest log.  There is 
	 * no expectation of any particular ordering of the keys.
	 * 
	 * @return The array of keys (quest names) from the quest log.
	 */
	public String[] keys() {
		String [] names = new String[this.count];
		int index = 0;

		// iterate through the hash table array
		for (int i =0;i<this.hashArray.length;i++){
			// if the linked list is not empty, iterate through the linked list to get the key of the quest log entry
			if (this.hashArray[i] != null )
			{
				this.hashArray[i].goFirst();

				while (this.hashArray[i].itemExists()){
					names[index] = this.hashArray[i].item().getQuestName();
					this.hashArray[i].goForth();
					index++;

				}
			}
		}

		return names;

		  // Remove this line you're ready.  It's just to prevent compiler errors.
	}
	
	/**
	 * Format the quest log as a string which displays the quests in the log in 
	 * alphabetical order by name.
	 * 
	 * @return A nicely formatted quest log.
	 */
	public String toString() {
		String info = "";
		// get all the names of the quest logs
		String [] names = this.keys();
		Arrays.sort(names);

		QuestLogEntry q;
		// for all the names, we obtain all the info of a quest log entry and print out the info.
		for (int i = 0;i<names.length;i++){

			q = obtain(names[i]);
			info  = info +  q.getQuestName() + " : "+ q.getQuestArea() + " ,Level Range: " +q.getRecommendedMinLevel() + "-"+ q.recommendedMaxLevel + "\n";

		}

		return info ;
	}
	
	/**
	 * Obtain the quest with name k, while simultaneously returning the number of
	 * items examined while searching for the quest.
	 * @param k Name of the quest to obtain.
	 * @return A pair in which the first item is the QuestLogEntry for the quest named k, and the
	 *         second item is the number of items examined during the search for the quest named k.
	 *         Note: if no quest named k is found, then the first item of the pair should be null.
	 */
	public Pair280<QuestLogEntry, Integer> obtainWithCount(String k) {
		int count = 0;
		Pair280<QuestLogEntry, Integer> result = new Pair280<>(null,count);


		// search method from keyedchainedhashtable280 file
		// find the quest log entry based on the given key.

		int itemHashLocation = this.hashPos(k);
		if (searchesContinue && itemListLocation!=null) goForth();
		else
		{
			// when the location is empty, we create a new chain at the position.
			if (hashArray[itemHashLocation]==null)
				hashArray[itemHashLocation] = newChain();
			itemListLocation = hashArray[itemHashLocation].iterator();
		}
		count++;

		while (k.compareTo(itemListLocation.item().key()) != 0 )
		{
			itemListLocation.goForth();
			count++;
		}
		if (itemExists()){
			result.setFirstItem(item());
			result.setSecondItem(count);

		}

		return result;
	

	}
	
	
	public static void main(String args[])  {
		// Make a new Quest Log
		QuestLog hashQuestLog = new QuestLog();
		
		// Make a new ordered binary lib280.tree.
		OrderedSimpleTree280<QuestLogEntry> treeQuestLog =
				new OrderedSimpleTree280<QuestLogEntry>();
		
		
		// Read the quest data from a CSV (comma-separated value) file.
		// To change the file read in, edit the argument to the FileReader constructor.
		CSVReader inFile;
		try {
			//input filename on the next line - path must be relative to the working directory reported above.
			inFile = new CSVReader(new FileReader("C:\\Users\\chaiyichai\\Desktop\\280\\asn4\\asn5-startercode\\QuestLog-Template2\\QuestLog-Template\\quests4.csv"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
			return;
		}
		
		String[] nextQuest;
		try {
			// Read a row of data from the CSV file
			while ((nextQuest = inFile.readNext()) != null) {
				// If the read succeeded, nextQuest is an array of strings containing the data from
				// each field in a row of the CSV file.  The first field is the quest name,
				// the second field is the quest region, and the next two are the recommended
				// minimum and maximum level, which we convert to integers before passing them to the
				// constructor of a QuestLogEntry object.
				QuestLogEntry newEntry = new QuestLogEntry(nextQuest[0], nextQuest[1], 
						Integer.parseInt(nextQuest[2]), Integer.parseInt(nextQuest[3]));
				// Insert the new quest log entry into the quest log.
				hashQuestLog.insert(newEntry);
				treeQuestLog.insert(newEntry);
			}
		} catch (IOException e) {
			System.out.println("Something bad happened while reading the quest information.");
			e.printStackTrace();
		}
		
		// Print out the hashed quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
		System.out.println(hashQuestLog);

		
		// Print out the lib280.tree quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
	    System.out.println(treeQuestLog.toStringInorder());

		System.out.println("My toString method output:");
		System.out.println(hashQuestLog.toString());


		// TODO Determine the average number of elements examined during access for hashed quest log.
	    // (call hashQuestLog.obtainWithCount() for each quest in the log and find average # of access)


		double total = 0.0;
		Pair280<QuestLogEntry, Integer> result;
		// obtains all the keys
		String[] names = hashQuestLog.keys();
		//literate the key list
		for (int i =0;i<names.length;i++){
			result = hashQuestLog.obtainWithCount(names[i]);
			if (result.firstItem()!= null){

				// the second item of a pair is the searching times
				total+= result.secondItem();

			}
		}
		double average =  total/ names.length;
		System.out.println("Avg . # of items examined per query in the hashed quest log with "+ names.length + " entries :"+ average);







		// TODO Determine the average number of elements examined during access for lib280.tree quest log.
	    // (call treeQuestLog.searchCount() for each quest in the log and find average # of access)

		double count = 0.0;

		for (int i =0;i< names.length;i++){

			QuestLogEntry q = hashQuestLog.obtain(names[i]);
			count += treeQuestLog.searchCount(q);
		}
		count = count / names.length;
		System.out.println("Avg . # of items examined per query in the tree quest log with "+ names.length+ " entries :"+count);




		
	}
	
	
}
