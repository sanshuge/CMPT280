package lib280.asn2;

import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;
import lib280.tree.MAryNode280;


/**
 name: Ruobing Fu
 NSID: xdt709
 student number: 11319234
 course number:CMPT-280-02
 */


public class SkillTree extends BasicMAryTree280<Skill> {

	/**
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.
	 *
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes
	 * @timing O(1)
	 */
	public SkillTree(Skill x, int m) {
		super(x, m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 *
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTree rootSubTree(int i) {
		return (SkillTree) super.rootSubtree(i);
	}


	/**
	 *
	 * @param skillname
	 * @return a linked list containing all the prerequisites skills for the skillname
	 * @throws RuntimeException
	 */
	public  LinkedList280<Skill> skillDependencies(String skillname) throws RuntimeException {
		LinkedList280<Skill> preSkills = new LinkedList280<>();
		if (!skillDependenciesHelper(skillname,this,preSkills)) throw new RuntimeException("Skill not found. ");
		return preSkills;

	}

	/**
	 *
	 * @param skillname a string represents the name of the skill
	 * @param skills a skill tree
	 * @param preSkills
	 * @return true if the skill is found.
	 */

	protected boolean skillDependenciesHelper(String skillname, SkillTree skills , LinkedList280<Skill> preSkills) {
		if (this.isEmpty()) {
			return false;
		}
		if (skills.rootItem().getSkillName().compareTo(skillname)==0) {
			preSkills.insert(skills.rootItem());// find the skill name
			return true;
		}
		for (int i = 1; i <= skills.rootLastNonEmptyChild(); i++) {

				if (skillDependenciesHelper(skillname,skills.rootSubTree(i),preSkills)) {
					preSkills.insert(skills.rootItem());// find the skill name
					return true;
				}
			}
			return false;
	}

	/**
	 *
	 * @param skillname
	 * @return an integer represent the total cost
	 */
	public int skillTotalCost(String skillname){
		int totalCost = 0;
		LinkedList280<Skill> Preskills = skillDependencies(skillname);
		Preskills.goFirst();
		while (Preskills.itemExists()){
			totalCost = totalCost + Preskills.item().getSkillCost();
			Preskills.goForth();
		}
		return totalCost;
	}





	public static void main(String[] args) {


		//the following skills are inspired by  Twilight series
		// Preskill1 is technically not a skill but you have to be a grown-up vampire to have the various skills.
		Skill PreSkill1 = new Skill("A mature vampire"," Being a mature vampire ",10);
		Skill skill2 = new Skill("Illusion generation","The skill to create illusion for the enemy.",3);
		Skill skill3 = new Skill("Weather control","The skill to control weather,eg: temperature, humidity,moisture.",4);
		Skill skill4 = new Skill("Speed","The skill to move, react at speeds that can barely be detected by human eyes.",3);
		Skill skill5 = new Skill("Sensory Deprivation","The ability to cut off all physical senses ",3);
		Skill skill6 = new Skill("Mental shield","The skill to create a shield to block all kinds of powers that act on the mind",6);
		Skill skill7 = new Skill("Vertigo","The skill to create illusion.",9);
		Skill skill8 = new Skill("Precognition","The skill to forsee the future based on the decisions have been made",19 );
		Skill skill9 = new Skill("Cryokinesis","The skill to reduce the temperature of a matter, causing it to freeze",12);
		Skill skill10 = new Skill("Electrical Control","The skill to create and manipulate electricity and lightning.",15);
		Skill skill11 = new Skill("Phobikinesis","The skill to project fear into the minds of others",10);
		Skill skill12 = new Skill("Pain illusion","The skill to trick other's mind in to thinking they are in huge pain",21);



		SkillTree T = new SkillTree(PreSkill1,4);
		T.setRootSubtree(new BasicMAryTree280<>(skill2,3) ,1);
		T.setRootSubtree(new BasicMAryTree280<>(skill7,3) ,2);
		T.setRootSubtree(new BasicMAryTree280<>(skill9,3) ,3);
		T.setRootSubtree(new BasicMAryTree280<>(skill10,3) ,4);
		BasicMAryTree280<Skill> Skill2 = T.rootSubtree(1);
		Skill2.setRootSubtree(new BasicMAryTree280<>(skill4,3) ,1);
		Skill2.setRootSubtree(new BasicMAryTree280<>(skill3,3) ,2);
		BasicMAryTree280<Skill> Skill7= T.rootSubtree(2);
		Skill7.setRootSubtree(new BasicMAryTree280<>(skill8,3) ,1);
		BasicMAryTree280<Skill> Skill8= Skill7.rootSubtree(1);
		Skill8.setRootSubtree(new BasicMAryTree280<>(skill11,3),1);
		BasicMAryTree280<Skill> Skill9 = T.rootSubtree(3);
		Skill9.setRootSubtree(new BasicMAryTree280<>(skill12,3),1);
		BasicMAryTree280<Skill> Skill10 = T.rootSubtree(4);
		Skill10.setRootSubtree(new BasicMAryTree280<>(skill5,3),1);
		Skill10.setRootSubtree(new BasicMAryTree280<>(skill6,3),2);
		System.out.println("Skill tree: ");
		System.out.println(T.toStringByLevel());

		// test cases:


		String expected;
		expected="A mature vampire, Cost: 10, Illusion generation, Cost: 3, Speed, Cost: 3, ";
		if (!T.skillDependencies("Speed").toString().equals(expected)){
			System.out.println("Error: the prerequisites skills for Speed is wrong. ");
		}
		expected = "A mature vampire, Cost: 10, Vertigo, Cost: 9, Precognition, Cost: 19, Phobikinesis, Cost: 10, ";
		if (!T.skillDependencies("Phobikinesis").toString().equals(expected)){
			System.out.println("Error: the prerequisites skills for Phobikinesis is wrong. ");
		}

		try {
			T.skillDependencies("Shield Bash");
		}
		catch (RuntimeException e){
			System.out.println("Shield Bash is not found" );
		}

		int expectedCost;

		expectedCost = 31;
		if (T.skillTotalCost("Mental shield")!=expectedCost){

			System.out.println("Error: the total cost of Mental Shield is wrong. ");
		}
		expectedCost=22;
		if (T.skillTotalCost("Cryokinesis")!=expectedCost){

			System.out.println("Error: the total cost of Cryokinesis is wrong. ");
		}
		try {
			T.skillTotalCost("Tactile Telepathy");
		}
		catch (RuntimeException e){
			System.out.println("Tactile Telepathy is not found" );
		}

		System.out.println("prerequisites skills for Electrical Control");
		System.out.println(T.skillDependencies("Electrical Control").toString());
		System.out.println("Total cost for Electrical Control is "+ T.skillTotalCost("Electrical Control"));


		System.out.println("prerequisites skills for Illusion generation");
		System.out.println(T.skillDependencies("Illusion generation").toString());
		System.out.println("Total cost for Illusion generation is "+ T.skillTotalCost("Illusion generation"));












	}
}
	
	


