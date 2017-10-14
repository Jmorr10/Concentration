import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.html.HTMLEditorKit;

// TODO Fix Joker Match bug!


@SuppressWarnings("serial")
public class Concentration extends JPanel implements MouseListener,
		ActionListener {

	static int userPoints = 0;
	static int opponentPoints = 0;
	static int choice = 0;
	static int card1 = 0;
	static int card2 = 0;
	static int tCount = 0;
	static String gameVariation = "Standard";
	static String numPlayers = "One Player";
	static Boolean userTurn = true;
	static JFrame application = new JFrame("Concentration by Joseph Morris");
	static Concentration game = new Concentration();
	static JLabel[] CardFaces = new JLabel[54];
	static JLabel[] CardBacks = new JLabel[54];
	static JMenuBar menuBar = new JMenuBar();
	static GridLayout grid = new GridLayout(6, 9) {
		{
			this.setHgap(10);
			this.setVgap(10);
		}
	};
	static JPanel cardPanel = new JPanel(grid);
	static String userName = "";
	static String opponentName = "";
	static Card emptyCard = game.new Card();
	static JLabel eSpace = new JLabel();
	static String[] mainArgs = new String[4];
	
	static int startNum = 1;
	static Boolean skip = false;
	static int matchesFound = 0;
	static ArrayList<Object[]> memory = new ArrayList<Object[]>();
	static ArrayList<Object[]> userCardMemory = new ArrayList<Object[]>();
	static ArrayList<Object[]> memory2 = new ArrayList<Object[]>();
	static ArrayList<Object[]> userCardMemory2 = new ArrayList<Object[]>();
	static JPanel logPanel = new JPanel();
	static int computerChoices = 0;
	static int computerCard1 = 0;
	static int computerCard2 = 0;
	
	
	public Concentration() {

		setLayout(new BorderLayout());
	}

	private class Card {

		protected int value = 0;
		protected String color = "";
		protected String suit = "";
		protected String card = "";

		public Card() {

			this.value = 0;
			this.color = "Empty";
			this.suit = "Empty";
			this.card = "Empty";

		}

		public Card(int val, String suit) {

			this.value = val;
			this.suit = suit;
			if (this.suit.equals("clubs") || this.suit.equals("spades")
					|| this.suit.equals("Black")) {
				this.color = "Black";
			} else if (this.suit.equals("hearts")
					|| this.suit.equals("diamonds") || this.suit.equals("Red")) {
				this.color = "Red";
			}
			switch (val) {

			case 1:
				this.card = "Ace of " + this.suit;
				break;
			case 2:
				this.card = "Two of " + this.suit;
				break;
			case 3:
				this.card = "Three of " + this.suit;
				break;
			case 4:
				this.card = "Four of " + this.suit;
				break;
			case 5:
				this.card = "Five of " + this.suit;
				break;
			case 6:
				this.card = "Six of " + this.suit;
				break;
			case 7:
				this.card = "Seven of " + this.suit;
				break;
			case 8:
				this.card = "Eight of " + this.suit;
				break;
			case 9:
				this.card = "Nine of " + this.suit;
				break;
			case 10:
				this.card = "Ten of " + this.suit;
				break;
			case 11:
				this.card = "Jack of " + this.suit;
				break;
			case 12:
				this.card = "Queen of " + this.suit;
				break;
			case 13:
				this.card = "King of " + this.suit;
				break;
			case 14:
				this.card = this.color + " Joker";
				break;

			}
		}

		public String toString() {
			return this.card;
		}
	}

	static ArrayList<Card> deck = new ArrayList<Card>();

	public static void main(String... args) {
		
		Boolean valid = false;

		if (!skip) {

			do {
				try {
					String start = JOptionPane
							.showInputDialog("Please enter the number of players (0, 1, or 2):");
					startNum = Integer.parseInt(start);
					if (start.equals(null) || start.equals("") || startNum > 2
							|| startNum < 0) {
						valid = false;
					} else {
						valid = true;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(game, "Entry not valid!");
				}
			} while (!valid);

		}
		 
		if (args.length == 0 || args.length <= 2) {
			mainArgs[0] = "closed";
			switch (startNum) {
			case 0:
				mainArgs[1] = "Zero Players";
				break;
			case 1:
				mainArgs[1] = "One Player";
				break;
			case 2:
				mainArgs[1] = "Two Players";
				break;
			}
			numPlayers = mainArgs[1];
			mainArgs[2] = "Standard";
			gameVariation = mainArgs[2];
		} else {
			mainArgs = args;
		}
		// Reset to Defaults
		
		application.remove(cardPanel);
		application.validate();
		menuBar.removeAll();
		userPoints = 0;
		opponentPoints = 0;
		choice = 0;
		card1 = 0;
		card2 = 0;
		tCount = 0;
		matchesFound = 0;
		userTurn = true;
		// End Reset

		BorderLayout bLayout = new BorderLayout();
		application.setLayout(bLayout);
		deck.clear();
		game.generateDeck();
		shuffleDeck(deck);
		
		valid = false;
		if (mainArgs.length != 0) {
			if (mainArgs[1].equals("One Player")) {
				do {
					try {
						userName = JOptionPane
								.showInputDialog("Please enter your name:");
						if (userName.equals(null) || userName.equals("")) {
							valid = false;
						} else {
							valid = true;
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(game, "Entry not valid!");
					}
				} while (!valid);
				opponentName = "Computer";
			} else if (mainArgs[1].equals("Two Players")) {
				do {
					try {
						userName = JOptionPane
								.showInputDialog("Please enter your name:");
						if (userName.equals(null) || userName.equals("")) {
							valid = false;
						} else {
							valid = true;
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(game, "Entry not valid!");
					}
				} while (!valid);
				valid = false;
				do {
					try {
						opponentName = JOptionPane
								.showInputDialog("Please enter your opponent's name:");
						if (opponentName.equals(null)
								|| opponentName.equals("")) {
							valid = false;
						} else {
							valid = true;
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(game, "Entry not valid!");
					}
				} while (!valid);
				valid = false;
			} else if (mainArgs[1].equals("Zero Players")) {
				userName = "Computer #1";
				opponentName = "Computer #2";
			} else {
				do {
					try {
						userName = JOptionPane
								.showInputDialog("Please enter your name:");
						if (userName.equals(null) || userName.equals("")) {
							valid = false;
						} else {
							valid = true;
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(game, "Entry not valid!");
					}
				} while (!valid);
				opponentName = "Computer";
				valid = false;
			}
		} else {
			do {
				try {
					userName = JOptionPane
							.showInputDialog("Please enter your name:");
					if (userName.equals(null) || userName.equals("")) {
						valid = false;
					} else {
						valid = true;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(game, "Entry not valid!");
				}
			} while (!valid);
			opponentName = "Computer";
			valid = false;
		}

		refreshCardPanel(mainArgs);
		game.removeAll();
		
		game.add(cardPanel, BorderLayout.CENTER);
		JPanel dummy = setupStatusBar(userName, opponentName);
		game.add(dummy, BorderLayout.WEST);
		
		application.setJMenuBar(setupMenu());
		application.getContentPane().add(game);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.validate();
		application.pack();
		application.setLocationRelativeTo(null);
		application.setVisible(true);
		if (mainArgs[1].equals("Zero Players")) {
			userTurn = true;
			computerTurn();
			
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().toString().equals("New Game")) {
			skip = true;
			if (numPlayers.contains("Zero"))
				startNum = 0;
			if (numPlayers.contains("One"))
				startNum = 1;
			if (numPlayers.contains("Two"))
				startNum = 2;
			main(mainArgs[0], numPlayers, gameVariation);
			return;
		} else if (e.getActionCommand().toString().equals("Standard")) {
			gameVariation = "Standard";
			JOptionPane.showMessageDialog(game,
					"Click new game to apply these settings!");
		} else if (e.getActionCommand().toString().equals("Any Color")) {
			gameVariation = "Any Color";
			JOptionPane.showMessageDialog(game,
					"Click new game to apply these settings!");
		} else if (e.getActionCommand().toString().equals("One Flip")) {
			gameVariation = "One Flip";
			JOptionPane.showMessageDialog(game,
					"Click new game to apply these settings!");
		} else if (e.getActionCommand().toString().equals("Zebra")) {
			gameVariation = "Zebra";
			JOptionPane.showMessageDialog(game,
					"Click new game to apply these settings!");
		} else if (e.getActionCommand().toString().equals("Zero Players")) {
			numPlayers = "Zero Players";
			JOptionPane.showMessageDialog(game,
					"Click new game to apply these settings!");
		} else if (e.getActionCommand().toString().equals("One Player")) {
			numPlayers = "One Player";
			JOptionPane.showMessageDialog(game,
					"Click new game to apply these settings!");
		} else if (e.getActionCommand().toString().equals("Two Players")) {
			numPlayers = "Two Players";
			JOptionPane.showMessageDialog(game,
					"Click new game to apply these settings!");
		} else if (e.getActionCommand().toString().equals("Exit")) {
			System.exit(0);
		} else if (e.getActionCommand().toString().equals("How to Play")) {

			JEditorPane helpPane = new JEditorPane();

			helpPane.setEditorKit(new HTMLEditorKit());
			helpPane.setText("<HTML><body><p>Rules<br><br>Any deck of playing cards may be used, although there are special cards available, as shown in the picture above.<br>The rules given here are for a standard deck of 52 cards, which are normally laid face down in 4 rows of 13 cards each.<br>The two jokers may be included for a tableau of 6 rows of 9 cards each.<br>In turn each player chooses two cards and turns them face up. If they are of the same rank and color (e.g. 6 of hearts and<br> 6 of diamonds, queen of clubs and queen of spades, or both jokers, if used) then that player wins the pair and plays again.<br>If they are not of the same rank and color, they are turned face down again and play passes to the player on the left.<br>The game ends when the last pair has been picked up. The winner is the person with the most pairs, and there may be a tie for first place.</p><br><br>Source: en.wikipedia.org/wiki/Concentration_(game)</body></HTML>");
			JScrollPane scroll = new JScrollPane(helpPane);
			helpPane.setEditable(false);
			JFrame helpFrame = new JFrame("Help");
			helpFrame.add(scroll);
			helpFrame.pack();
			helpFrame.setVisible(true);

		} else if (e.getActionCommand().toString().equals("About")) {

			JOptionPane
					.showMessageDialog(
							game,
							"This program was created by Joseph Morris of the University of Rochester, Class of 2012.\n For more information email Jmorr10@u.rochester.edu",
							"About", JOptionPane.INFORMATION_MESSAGE);

		}

	}

	public static void computerTurn() {
		Boolean selectionsMade = false;
		computerChoices = 0;
		computerCard1 = 0;
		computerCard2 = 0;
		//If nothing in memory, select randomly!
		if (memory.size() == 0) { 
			
			do {
				
				Random rand = new Random();
				int select = rand.nextInt(54);
				
				if (!(cardPanel.getComponent(select).getName().equals("Empty")) && computerChoices == 0) {
					computerCard1 = select;
					computerChoices +=1;
					selectionsMade = false;
				}
				
				if (!(cardPanel.getComponent(select).getName().equals("Empty")) && computerChoices == 1 && select != computerCard1) {
					computerCard2 = select;
					computerChoices +=1;
					selectionsMade = true;
				} 
				
			} while (!selectionsMade);
			//Make sure two random cards were selected, if not throw error and terminate!
			if (computerChoices == 2) {
				
				//Make sure cardPanel isn't null, if it is throw error and terminate!
				if (cardPanel.getComponents().length > 0) {
					
					final Runnable sourceAction = new Runnable() {
						
						public void run() {
							cardPanel.getComponent(computerCard1).dispatchEvent(new MouseEvent(cardPanel.getComponent(computerCard1), MouseEvent.MOUSE_CLICKED,0,0,cardPanel.getComponent(computerCard1).getX(),cardPanel.getComponent(computerCard1).getY(),1, false));		
						}
						
					};
					
					
					Runnable flip1Runnable = new Runnable() {
						public void run() {
						try {
							EventQueue.invokeAndWait(sourceAction);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}							
						}
						
					};
					
					Thread doAction = new Thread(flip1Runnable);
					doAction.start();
					
					final Runnable sourceAction2 = new Runnable() {
						
						public void run() {
							cardPanel.getComponent(computerCard2).dispatchEvent(new MouseEvent(cardPanel.getComponent(computerCard2), MouseEvent.MOUSE_CLICKED,0,0,cardPanel.getComponent(computerCard2).getX(),cardPanel.getComponent(computerCard2).getY(),1, false));		
						}
						
					};
					
					
					Runnable flip2Runnable = new Runnable() {
						public void run() {
						try {
							EventQueue.invokeAndWait(sourceAction2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}							
						}
						
					};
					
					Thread doAction2 = new Thread(flip2Runnable);
					doAction2.start();
					
					
				} else {
					JOptionPane.showMessageDialog(game, "Null cardPanel! Terminating!","Error!", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				
			} else {
				JOptionPane.showMessageDialog(game, "An error occurred in random card selection! Terminating!","Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			
		} else {
			//If there ARE cards in memory... Use this information to check for cards, then remove the elements!
			
			Object[] removeMemory = new Object[3];
			Object[] removeMemory2 = new Object[3];
			Object[] removeUserCardMemory = new Object[3];
			
			for (Object[] a : memory) {
				for (Object[] b : memory) {
					//If value and color match, then select those from memory!
					if (a[1].equals(b[1]) && a[2].equals(b[2]) && a[0] != b[0]) {
						computerCard1 = Integer.parseInt(a[0].toString());
						computerCard2 = Integer.parseInt(b[0].toString());
						computerChoices = 2;
						removeMemory = a;
						removeMemory2 = b;
						break;
					}
					if (computerChoices == 2) {
						break;
					}
				}
			}
			
			if (computerChoices == 2) {
				memory.remove(removeMemory);
				memory.remove(removeMemory2);
			}
			
			//If computerChoice != 2, then check memory against userCard memory
			if (computerChoices != 2) {
				for (Object[] a : memory) {
					for (Object[] b : userCardMemory) {
						//If value and color match, then select those from memory!
						if (a[1].equals(b[1]) && a[2].equals(b[2]) && a[0] != b[0]) {
							computerCard1 = Integer.parseInt(a[0].toString());
							computerCard2 = Integer.parseInt(b[0].toString());
							computerChoices = 2;
							removeMemory = a;
							removeUserCardMemory = b;
							break;
						}
						if (computerChoices == 2) {
							break;
						}
					}
				}
		
			if (computerChoices == 2) {
				memory.remove(removeMemory);
				userCardMemory.remove(removeUserCardMemory);
			}
			
			
		}
	
		if (computerChoices != 2) {
			
			//If there is still not two card selections by this point, pick two random cards...
			do {
				
				Random rand = new Random();
				int select = rand.nextInt(54);
				
				if (!(cardPanel.getComponent(select).getName().equals("Empty")) && computerChoices == 0) {
					computerCard1 = select;
					computerChoices +=1;
					selectionsMade = false;
				}
				
				if (!(cardPanel.getComponent(select).getName().equals("Empty")) && computerChoices == 1 && select != computerCard1) {
					computerCard2 = select;
					computerChoices +=1;
					selectionsMade = true;
				} 
				
			} while (!selectionsMade);
			//Make sure two random cards were selected, if not throw error and terminate!
			if (computerChoices == 2) {
				
				//Make sure cardPanel isn't null, if it is throw error and terminate!
				if (cardPanel.getComponents().length > 0) {
					
					final Runnable sourceAction = new Runnable() {
						
						public void run() {
							cardPanel.getComponent(computerCard1).dispatchEvent(new MouseEvent(cardPanel.getComponent(computerCard1), MouseEvent.MOUSE_CLICKED,0,0,cardPanel.getComponent(computerCard1).getX(),cardPanel.getComponent(computerCard1).getY(),1, false));		
						}
						
					};
					
					
					Runnable flip1Runnable = new Runnable() {
						public void run() {
						try {
							EventQueue.invokeAndWait(sourceAction);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}							
						}
						
					};
					
					Thread doAction = new Thread(flip1Runnable);
					doAction.start();
					
					final Runnable sourceAction2 = new Runnable() {
						
						public void run() {
							cardPanel.getComponent(computerCard2).dispatchEvent(new MouseEvent(cardPanel.getComponent(computerCard2), MouseEvent.MOUSE_CLICKED,0,0,cardPanel.getComponent(computerCard2).getX(),cardPanel.getComponent(computerCard2).getY(),1, false));		
						}
						
					};
					
					
					Runnable flip2Runnable = new Runnable() {
						public void run() {
						try {
							EventQueue.invokeAndWait(sourceAction2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}							
						}
						
					};
					
					Thread doAction2 = new Thread(flip2Runnable);
					doAction2.start();
					
					
					
				} else {
					JOptionPane.showMessageDialog(game, "Null cardPanel! Terminating!","Error!", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				
			} else {
				JOptionPane.showMessageDialog(game, "An error occurred in random card selection! Terminating!","Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			
		} else {
			
			//If there ARE two selections, then dispatch mouse clicks!
			if (cardPanel.getComponents().length > 0) {
				
				//Dispatch mouse clicks!
				
				final Runnable sourceAction = new Runnable() {
					
					public void run() {
						cardPanel.getComponent(computerCard1).dispatchEvent(new MouseEvent(cardPanel.getComponent(computerCard1), MouseEvent.MOUSE_CLICKED,0,0,cardPanel.getComponent(computerCard1).getX(),cardPanel.getComponent(computerCard1).getY(),1, false));		
					}
					
				};
				
				
				Runnable flip1Runnable = new Runnable() {
					public void run() {
					try {
						EventQueue.invokeAndWait(sourceAction);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}							
					}
					
				};
				
				Thread doAction = new Thread(flip1Runnable);
				doAction.start();
				
				final Runnable sourceAction2 = new Runnable() {
					
					public void run() {
						cardPanel.getComponent(computerCard2).dispatchEvent(new MouseEvent(cardPanel.getComponent(computerCard2), MouseEvent.MOUSE_CLICKED,0,0,cardPanel.getComponent(computerCard2).getX(),cardPanel.getComponent(computerCard2).getY(),1, false));		
					}
					
				};
				
				
				Runnable flip2Runnable = new Runnable() {
					public void run() {
					try {
						EventQueue.invokeAndWait(sourceAction2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}							
					}
					
				};
				
				Thread doAction2 = new Thread(flip2Runnable);
				doAction2.start();
				
				
			} else {
				JOptionPane.showMessageDialog(game, "Null cardPanel! Terminating!","Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			
		} 
			
	}
		
}	

	
	public static void computerTurnEvaluation() {
		
		if (choice == 2) {
		
			// TODO PROCESSING OF CARD INFORMATION HERE!
			
			if ((deck.get(card1).value == deck.get(card2).value
					&& deck.get(card1).color.equals(deck.get(card2).color) 
					|| (deck.get(card1).card.contains("Joker") && deck.get(card2).card.contains("Joker")))) {
				JOptionPane.showMessageDialog(logPanel, opponentName + " found a match!");
				opponentPoints += 1;
				matchesFound++;
				tCount += 1;
				deck.remove(card1);
				deck.add(card1, emptyCard);
				deck.remove(card2);
				deck.add(card2, emptyCard);
				JPanel dummy2 = setupStatusBar(userName, opponentName);
				game.removeAll();
				refreshCardPanel(mainArgs);
				userTurn = false;
				game.add(cardPanel);
				game.add(dummy2, BorderLayout.WEST);
				game.validate();
				choice = 0;
				card1 = 0;
				card2 = 0;
				computerChoices = 0;
				computerCard1 = 0;
				computerCard2 = 0;
				if (matchesFound == 27) {
					gameOver();
					return;
				}
				if (mainArgs[1].equals("Zero Players")) {
					computerTurn();
					return;
				}
				
				if (!userTurn) {
					computerTurn();
					return;
				}
			
			} else {

				double remember = Math.random();
				if (remember <= .6) {
					
					Object[] info = new Object[3];
					info[0] = card1;
					info[1] = deck.get(card1).value;
					info[2] = deck.get(card1).color;
					
					memory.add(info);
					
				}
				
				
				
				JOptionPane.showMessageDialog(logPanel, "No match...");
				tCount += 1;
				CardBacks[card1].setIcon(new ImageIcon("cardImages/"
						+ "Back.gif"));
				CardBacks[card2].setIcon(new ImageIcon("cardImages/"
						+ "Back.gif"));
				if (mainArgs[1].equals("Zero Players") && userTurn) {
					userTurn = false;
				} else if (mainArgs[1].equals("Zero Players") && !userTurn) {
					userTurn = true;
						
				} else if (mainArgs[1].equals("One Player") && !userTurn) {
					userTurn = true;
				}
				JPanel dummy2 = setupStatusBar(userName, opponentName);
				game.removeAll();
				refreshCardPanel(mainArgs);
				game.add(cardPanel);
				game.add(dummy2, BorderLayout.WEST);
				game.validate();
				choice = 0;
				card1 = 0;
				card2 = 0;
				computerChoices = 0;
				computerCard1 = 0;
				computerCard2 = 0;
				if (mainArgs[1].equals("Zero Players")) {
					computerTurn();
					return;
				}
				
				
			}
			
			

			
			/*for( Object[] a : memory) {
				for (Object x : a) {
					System.out.println(x.toString());
				}
			}
			*/
		}

	}

	
	public void generateDeck() {

		for (int suit = 1; suit <= 4; suit++) {

			for (int num = 1; num <= 13; num++) {
				if (suit == 1) {
					Card temp = this.new Card(num, "hearts");
					deck.add(temp);
				}
				if (suit == 2) {
					Card temp = this.new Card(num, "diamonds");
					deck.add(temp);
				}
				if (suit == 3) {
					Card temp = this.new Card(num, "spades");
					deck.add(temp);
				}
				if (suit == 4) {
					Card temp = this.new Card(num, "clubs");
					deck.add(temp);
				}

			}

		}

		deck.add(this.new Card(14, "Red"));
		deck.add(this.new Card(14, "Black"));

	}

	public static void gameOver() {

		if (userPoints > opponentPoints) {
			JOptionPane.showMessageDialog(game, userName + " wins!");
			JOptionPane.showMessageDialog(game, "Thanks for playing!");
			System.exit(0);
		}

		if (opponentPoints > userPoints) {
			JOptionPane.showMessageDialog(game, opponentName + " wins!");
			JOptionPane.showMessageDialog(game, "Thanks for playing!");
			System.exit(0);
		}

		if (userPoints == opponentPoints) {
			JOptionPane.showMessageDialog(game, "It's a tie!");
			JOptionPane.showMessageDialog(game, "Thanks for playing!");
			System.exit(0);
		}

	}

	public void mouseClicked(MouseEvent e) {
		
		int index = 0;
		for (int i = 0; i <= 53; i++) {
			if (cardPanel.getComponent(i).getName().equals(e.getComponent().getName())) {
				index = i;
			}
		}

		CardBacks[index].setIcon(new ImageIcon("cardImages/" + CardBacks[index].getName() + ((CardBacks[index].getName().contains("Joker")) ? ".png" : ".gif")));
		
		choice += 1;
		if (choice == 1) {
			card1 = index;
		}
		if (choice == 2) {
			card2 = index;
		}

		if (choice == 2) {
		if (mainArgs.length != 0) {
			if (mainArgs[1].equalsIgnoreCase("Zero Players")) {
				computerTurnEvaluation();
				return;
			} else if (mainArgs[1].equals("One Player") && userTurn) {
				userTurn();
				return;
			} else if (mainArgs[1].equals("One Player") && !userTurn) {
				computerTurnEvaluation();
				return;
			} else if (mainArgs[1].equals("Two Players") && userTurn) {
				userTurn();
				return;
			} else if (mainArgs[1].equals("Two Players") && !userTurn) {
				opponentTurn();
				return;
			}

		}
	}

}

	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	
	public static void opponentTurn() {

		if (choice == 2) {
			// TODO PROCESSING OF CARD INFORMATION HERE!

			if (card1 == card2) {
				JOptionPane.showMessageDialog(game,
						"You must select two different cards!");
				choice = 1;
				return;
			}

			if (deck.get(card1).value == deck.get(card2).value
					&& deck.get(card1).color.equals(deck.get(card2).color)) {
				JOptionPane.showMessageDialog(logPanel, "You found a match!");
				opponentPoints += 1;
				tCount += 1;
				matchesFound++;
				deck.remove(card1);
				deck.add(card1, emptyCard);
				deck.remove(card2);
				deck.add(card2, emptyCard);
				refreshCardPanel(mainArgs);
				cardPanel.validate();
				
				// TODO VARIATION (ONE-FLIP CODE HERE!!!)
				userTurn = false;
				if (matchesFound == 27) {
					gameOver();
					return;
				}
			} else {

				JOptionPane.showMessageDialog(logPanel, "No match...");
				tCount += 1;
				CardBacks[card1].setIcon(new ImageIcon("cardImages/"
						+ "Back.gif"));
				CardBacks[card2].setIcon(new ImageIcon("cardImages/"
						+ "Back.gif"));
				userTurn = true;
			}

			JPanel dummy2 = setupStatusBar(userName, opponentName);
			game.removeAll();
			refreshCardPanel(mainArgs);
			game.add(cardPanel);
			game.add(dummy2, BorderLayout.WEST);
			game.validate();
			choice = 0;
			card1 = 0;
			card2 = 0;

		}

	}
	
	public static void refreshCardPanel(String[] args) {
		cardPanel.removeAll();
		for (int x = 0; x <= 53; x++) {
			CardFaces[x] = new JLabel(new ImageIcon("cardImages/"
					+ deck.get(x).card
					+ (deck.get(x).card.contains("Joker") ? ".png" : ".gif")));
			CardFaces[x].setName(deck.get(x).card);
			if (deck.get(x).card.equalsIgnoreCase("Empty")) {
				CardBacks[x] = new JLabel();
			} else {
				CardBacks[x] = new JLabel(new ImageIcon("cardImages/"
						+ "Back.gif"));
			}
			if (!deck.get(x).card.equalsIgnoreCase("Empty")) {
				CardFaces[x].addMouseListener(game);
				CardBacks[x].addMouseListener(game);
			}
			CardBacks[x].setName(deck.get(x).card);
			if (mainArgs.length != 0) {
				if (args[0].equalsIgnoreCase("open")
						|| args[0].equalsIgnoreCase("o")) {
					cardPanel.add(CardFaces[x]);
				} else {
					cardPanel.add(CardBacks[x]);
				}
			} 	
		}
		cardPanel.validate();
	}

	public static JMenuBar setupMenu() {
		// Begin Menu Setup

		menuBar.setBackground(new Color(70, 45, 255));
		JMenu menuGame = new JMenu("Game");
		menuGame.setMnemonic(KeyEvent.VK_G);
		JMenuItem newGame = new JMenuItem("New Game", KeyEvent.VK_N);
		newGame.addActionListener(game);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.ALT_MASK));
		
		
		JMenu settings = new JMenu("Variations");
		settings.setMnemonic(KeyEvent.VK_S);
		JMenu players = new JMenu("Players");
		players.setMnemonic(KeyEvent.VK_P);

		//ButtonGroup variations = new ButtonGroup();
		
		ButtonGroup playersGrp = new ButtonGroup();

		/*
		
		JRadioButtonMenuItem standard = new JRadioButtonMenuItem("Standard");
		if (mainArgs[2].contains("Standard"))
			standard.setSelected(true);
		standard.addActionListener(game);
		variations.add(standard);

		JRadioButtonMenuItem anyColor = new JRadioButtonMenuItem("Any Color");
		if (mainArgs[2].contains("Any"))
			anyColor.setSelected(true);
		anyColor.addActionListener(game);
		variations.add(anyColor);

		JRadioButtonMenuItem oneFlip = new JRadioButtonMenuItem("One Flip");
		if (mainArgs[2].contains("One"))
			oneFlip.setSelected(true);
		oneFlip.addActionListener(game);
		variations.add(oneFlip);

		JRadioButtonMenuItem zebra = new JRadioButtonMenuItem("Zebra");
		if (mainArgs[2].contains("Zebra"))
			zebra.setSelected(true);
		zebra.addActionListener(game);
		variations.add(zebra);
*/
		JRadioButtonMenuItem zero = new JRadioButtonMenuItem("Zero Players");
		if (mainArgs[1].contains("Zero"))
			zero.setSelected(false);
		zero.addActionListener(game);
		playersGrp.add(zero);

		JRadioButtonMenuItem one = new JRadioButtonMenuItem("One Player");
		if (mainArgs[1].contains("One"))
			one.setSelected(true);
		one.addActionListener(game);
		playersGrp.add(one);

		JRadioButtonMenuItem two = new JRadioButtonMenuItem("Two Players");
		if (mainArgs[1].contains("Two"))
			two.setSelected(true);
		two.addActionListener(game);
		playersGrp.add(two);

		/*
		settings.add(standard);
		settings.add(anyColor);
		settings.add(oneFlip);
		settings.add(zebra);
		 */
		
		players.add(zero);
		players.add(one);
		players.add(two);

		JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.ALT_MASK));
		exit.addActionListener(game);
		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);
		JMenuItem howToPlay = new JMenuItem("How to Play", KeyEvent.VK_P);
		howToPlay.addActionListener(game);
		howToPlay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		JMenuItem about = new JMenuItem("About", KeyEvent.VK_A);
		about.addActionListener(game);

		menuGame.add(newGame);
		menuGame.add(players);
		menuGame.add(settings);
		menuGame.add(exit);

		menuHelp.add(howToPlay);
		menuHelp.add(about);

		menuBar.add(menuGame);
		menuBar.add(menuHelp);

		// End Menu Setup

		return menuBar;

	}

	public static JPanel setupStatusBar(String userName, String opponentName) {
		// Begin Status Bar

		
		Border line = BorderFactory.createLineBorder(Color.black);
		TitledBorder border;
		border = BorderFactory.createTitledBorder(line, "Log");
		border.setTitleJustification(TitledBorder.CENTER);
		logPanel.setBorder(border);
		BoxLayout boxL = new BoxLayout(logPanel, BoxLayout.Y_AXIS);
		logPanel.setLayout(boxL);
		JLabel playerName = new JLabel(userName);
		JLabel turnStatus = new JLabel();
		if (userTurn) {
			turnStatus = new JLabel("Current Player: " + userName);
		} else {
			turnStatus = new JLabel("Current Player: " + opponentName);

		}
		JLabel blank = new JLabel("  ");
		JLabel blank2 = new JLabel("  ");
		JLabel userPointsLbl = new JLabel(userName + "'s points: " + userPoints);
		JLabel opponentNameLbl = new JLabel(opponentName);
		JLabel opponentPointsLbl = new JLabel(opponentName + "'s points: "
				+ opponentPoints);
		JLabel turnCount = new JLabel("Total turns: " + tCount);
		
		logPanel.setPreferredSize(new Dimension(200, application.getHeight()));
		logPanel.removeAll();
		logPanel.add(playerName);
		logPanel.add(userPointsLbl);
		logPanel.add(blank);
		logPanel.add(opponentNameLbl);
		logPanel.add(opponentPointsLbl);
		logPanel.add(blank2);
		logPanel.add(turnStatus);
		logPanel.add(turnCount);
		logPanel.add(blank);
		
	// End Status Bar

		return logPanel;

	}
	
	public static void shuffleDeck(ArrayList<Card> deck) {
		// Shuffle deck twice for good measure. (Uses pre-defined
		// Collection.shuffle() method)
		Collections.shuffle(deck);
		Collections.shuffle(deck);
	}

	public static void userTurn() {
		if (choice == 2) {
			// TODO PROCESSING OF CARD INFORMATION HERE!

			if (card1 == card2) {
				JOptionPane.showMessageDialog(game,
						"You must select two different cards!");
				choice = 1;
				return;
			}

			if ((deck.get(card1).value == deck.get(card2).value
					&& deck.get(card1).color.equals(deck.get(card2).color) 
					|| (deck.get(card1).card.contains("Joker") && deck.get(card2).card.contains("Joker")))) {
				
				JOptionPane.showMessageDialog(logPanel,"You found a match!");
				userPoints += 1;
				tCount += 1;
				matchesFound++;
				deck.remove(card1);
				deck.add(card1, emptyCard);
				deck.remove(card2);
				deck.add(card2, emptyCard);
				refreshCardPanel(mainArgs);
				
				userTurn = true;
				if (matchesFound == 27) {
					gameOver();
					return;
				}
			} else {
				int previousLength = userCardMemory.size();
				Object[] temp = new Object[3];
				temp[0] = card1;
				temp[1] = deck.get(card1).value;
				temp[2] = deck.get(card1).color;
				
				for (Object[] x : memory) {
					
					if (x[1] == temp[1] && x[2] == temp[2] && temp[0] != x[0] ) {
						userCardMemory.add(temp);
					}
				}

				temp = new Object[3];
				temp[0] = card2;
				temp[1] = deck.get(card2).value;
				temp[2] = deck.get(card2).color;
				
				for (Object[] x : memory) {
					
					if (x[1] == temp[1] && x[2] == temp[2] && temp[0] != x[0] ) {
						userCardMemory.add(temp);
					}
				}
				
				if (userCardMemory.size() == previousLength) {
					
					double remember = Math.random();
					if (remember <= .6) {
						
						double cardTR = Math.random();
						
						if (cardTR <= .5) {
						
						Object[] info = new Object[3];
						info[0] = card1;
						info[1] = deck.get(card1).value;
						info[2] = deck.get(card1).color;
						
						userCardMemory.add(info);
						} else {
							Object[] info = new Object[3];
							info[0] = card2;
							info[1] = deck.get(card2).value;
							info[2] = deck.get(card2).color;
							userCardMemory.add(info);
						}	
					}
					
				}
				
				JOptionPane.showMessageDialog(logPanel, "No match...");
				tCount += 1;
				CardBacks[card1].setIcon(new ImageIcon("cardImages/"
						+ "Back.gif"));
				CardBacks[card2].setIcon(new ImageIcon("cardImages/"
						+ "Back.gif"));
				
				userTurn = false;
				
			}

			JPanel dummy2 = setupStatusBar(userName, opponentName);
			game.removeAll();
			refreshCardPanel(mainArgs);
			game.add(cardPanel);
			game.add(dummy2, BorderLayout.WEST);
			game.validate();
			choice = 0;
			card1 = 0;
			card2 = 0;
			if (!userTurn && mainArgs[1].equals("One Player")) {
				computerTurn();
				return;
			}
			
		}

	}
	
}