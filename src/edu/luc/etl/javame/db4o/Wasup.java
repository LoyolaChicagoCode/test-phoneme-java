package edu.luc.etl.javame.db4o;
import java.util.Date;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Wasup {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("wasup");

		final ObjectContainer db = Db4o.openFile("wasup.dbo");

		while (true) {
			final ObjectSet result = db.query(Item.class);
			if (Math.random() < 0.5) {
				final Item item = new Item("wasssup");
				db.store(item);
			} else if (result.size() > 0) {
				db.delete(result.next());
				db.commit();
			}
			System.out.println(result.size() + " records " + new Date());
			Thread.sleep(100);
		}

	}

}

class Item {
	private String name;
	public Item(final String name) { this.name = name; }
	public String getName() { return name; }
}