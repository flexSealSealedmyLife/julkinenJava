import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;


class TextEditor extends JFrame{

	private static final long serialVersionUID = 1L;
	String infoMessage = "Tyhjaa tiedostoa ei kannata tallentaa!";
    String titleBar = "Virhe";
    private String[] substantiivi = {"Prinsessa", "Rupikonna", "Takapuoli", "Palatsi", "Siideri", "Olut", "Nappaimisto", "Kenka", "Sedan", "Auto", "Prinssi", "Kamera" };
    private String[] adjektiivi = {"Kunniotettava", "Likomarka", "Sininen", "Ummettava", "Suhahtava", "Lyllertava", "Rapsakka", "Kimalteleva", "Hurmaava", "Kauhea", "Ihmeellinen", "Mullistava" };	
															//Nama kaksi arrayta antavat arpoa inspiraatiota

    
	private TextEditor()
	{		
	
	JFrame frame = new JFrame("The Notepad 3000");			//Luodaan mainframe ja sen käyttäytyminen suljettaessa
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	
	JPanel mainPanel = new JPanel(new BorderLayout(50,0));	//Tehdaan kaikelle alusta, tahan lisataan muut elementit
	mainPanel.setBorder(BorderFactory.createLineBorder(Color.white, 10));
	
	JPanel nappiPanel = new JPanel(new GridLayout(1,1)); 	//Paneeli johon lisataan kaikki ohjelmassa tarvittavat napit
	nappiPanel.setBorder(BorderFactory.createLineBorder(Color.white, 10));
	
	JTabbedPane tabbedPane = new JTabbedPane();
	JTextArea JTArea = new JTextArea(10,10); 				//Tehdaan tekstinkasittely alue
	
	JLabel lb1, lb2;										//Alustetaan labelit jotka vastaavat kirjainten ja sanojen laskemista
	lb1=new JLabel("Kirjaimia: "); 
	lb2=new JLabel(" Sanoja: ");
															//Seuraava documentListener seuraa JTAreaa. Se laskee sanat ja kirjaimet 
															//joka actionin jälkeen ja päivittää lb1&2.
	DocumentListener documentListener = new DocumentListener() { 
	      public void changedUpdate(DocumentEvent documentEvent) {
	    	  changeIt(documentEvent);
	      }
	      public void insertUpdate(DocumentEvent documentEvent) {
	    	  changeIt(documentEvent);
	      }
	      public void removeUpdate(DocumentEvent documentEvent) {
	    	  changeIt(documentEvent);
	      }
	      private void changeIt(DocumentEvent documentEvent) {
	        Document source = documentEvent.getDocument();
	        int letters = source.getLength();	        
	        String words = "";
				try {
					words = source.getText(0,letters);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			String wordCount[]=words.split("\\s"); 			
	        lb1.setText("Kirjamia: "+ letters);
	        lb2.setText(" Sanoja: " + wordCount.length);
	      }
	    };
	    													//Lisätään JTArealle juuri luotu documentlistener
	JTArea.getDocument().addDocumentListener(documentListener);	
	
	JScrollPane scrollPane = new JScrollPane(JTArea);		 //lisataan tekstinkasittely alue jscrollpaneeliin, etta tekstia voidaan scrollata ja annetaan islle kiva raja
	scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	tabbedPane.addTab("Tekstinkäsittely", scrollPane);
	mainPanel.add(tabbedPane);	
	
	JButton tallenna = new JButton("Tallenna");				
	JButton avaa = new JButton("Avaa");
	JButton sulje = new JButton("Sulje");
	JButton arvo = new JButton("Arvo Aihe");
	
	JPanel countPanel = new JPanel(false);					//Luodaan tekstindata välilehti, sen elementit ja asettelu oikein suunnitelman mukaisesti.
	JPanel filler1 = new JPanel(false);
	JPanel collectAll = new JPanel(false);		
	collectAll.add(lb2);
	collectAll.add(lb1);
	countPanel.add(filler1);
	countPanel.add(collectAll);
	countPanel.setLayout(new GridLayout(2, 1));	
	tabbedPane.addTab("Laskurit", countPanel);
															//Seuraavaksi lisätään napit paneeliin ja laitetaan ne näyttämään hyvältä sekä
															//lisätään napeille niiden toiminnallisuus. Lopuksi frame näkyville.
	nappiPanel.add(sulje);	
	nappiPanel.add(Box.createRigidArea(new Dimension(2,0)));//Nama ovat filleria		
	sulje.addActionListener(new CloseListener());
	nappiPanel.add(avaa);
	nappiPanel.add(Box.createRigidArea(new Dimension(2,0)));//Nama ovat filleria	
	nappiPanel.add(arvo);
	nappiPanel.add(Box.createRigidArea(new Dimension(2,0)));//Nama ovat filleria
	arvo.addActionListener(new ArvoAihe());
	nappiPanel.add(tallenna);
	mainPanel.add(nappiPanel, BorderLayout.SOUTH);			//asetetaan nappiframe alareunaan.
	frame.setSize(680,850);
	frame.add(mainPanel);
	frame.setResizable(false);
	frame.setVisible(true);	
	
	avaa.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0) { 	//Tama aukaisee halutun tiedoston ja kirjoittaa sen sisallon textareaan
			FileDialog fd = new FileDialog(new JFrame());
			fd.setVisible(true);
			File[] f = fd.getFiles();
			if(f.length > 0){
			    try {
					FileReader reader = new FileReader(fd.getFiles()[0].getAbsolutePath());
					BufferedReader br = new BufferedReader(reader);
					try {
						JTArea.read(br, null);
						br.close();
						JTArea.requestFocus();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}			    
			}
		}
	});
		
	tallenna.addActionListener(new ActionListener() 		// tama tallentaa textarean sisallon txt tiedostoksi.
			{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String s = JTArea.getText();
					if (s.length() == 0)
					{
						new ToimintoLuokka().infoBox(infoMessage, titleBar);
					}else {
						String ss = ToimintoLuokka.tiedostonNimenKysely();
						File outputfile = new File(ss + ".txt") ;
						try {
					        BufferedWriter out = new BufferedWriter(new FileWriter(outputfile));
						    out.write(s);
						    out.close();
						    ToimintoLuokka.tallennusOnnistui();
				        }
				        catch (IOException e) {
				        	ToimintoLuokka.tallenusEpaonnistui();
				        	e.printStackTrace();
				        }
					}		
				}
			}
		);	
}


private static class ToimintoLuokka
{
	public static String tiedostonNimenKysely()
	{		
		String tiedostonNimi1 = JOptionPane.showInputDialog("Anna tiedostolle nimi:");
		return tiedostonNimi1;		
	}
	
    public void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "" + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void tallennusOnnistui()
    {
    	JOptionPane.showMessageDialog(null, "Tallennus onnistui!");
    }
    
    public static void tallenusEpaonnistui() 
    {
    	JOptionPane.showMessageDialog(null, "Tallennus Epäonnistui!");
    }
  
}

private class CloseListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}

private class ArvoAihe implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent eee) {

		Random rand = new Random();		
		int n = rand.nextInt(11) + 1;
		
	    JOptionPane optionPane = new JOptionPane(new JLabel(adjektiivi[n] +" " + substantiivi[n],JLabel.CENTER));
	    JDialog dialog = optionPane.createDialog("Inpiraatio generaattori");
	    dialog.setModal(true);
	    dialog.setVisible(true);
	}
}
	
	public static void main(String[] args)
	{
		JFrame tt = new TextEditor();
		tt.dispose();
	}
}
