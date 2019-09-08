/* AutomusicK by MiniMacro: minimacrodx@gmail.com
 * 2019
 * AutomusicK is a tool to generate a blank AddmusicK-compatible .txt file based on
 * your specification.
 */
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
public class AutomusicK extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JLabel successLabel = new JLabel("");
	private Checkbox ch0echo = new Checkbox("Channel 0 Echo", false);
	private Checkbox ch1echo = new Checkbox("Channel 1 Echo", false);
	private Checkbox ch2echo = new Checkbox("Channel 2 Echo", false);
	private Checkbox ch3echo = new Checkbox("Channel 3 Echo", false);
	private Checkbox ch4echo = new Checkbox("Channel 4 Echo", false);
	private Checkbox ch5echo = new Checkbox("Channel 5 Echo", false);
	private Checkbox ch6echo = new Checkbox("Channel 6 Echo", false);
	private Checkbox ch7echo = new Checkbox("Channel 7 Echo", false);
	private int echoNum = 0;
	private String echoHex = "00";
	
	private Checkbox path = new Checkbox("#path", false);
	private JTextField pathName = new JTextField(20);
	
	private Checkbox samples = new Checkbox("#samples", false);
	private String[] sampleTypes = { "none", "#default", "#optimized" };
	private JComboBox sampleTypesMenu = new JComboBox(sampleTypes);
	
	private Checkbox instruments = new Checkbox("#instruments", false);

	private JTextField author = new JTextField(20);
	private JTextField title = new JTextField(20);
	private JTextField game = new JTextField(20);
	private JTextField comment = new JTextField(20);
	
	private JTextField tempo = new JTextField(20);

	public AutomusicK() throws IOException
	{
		setLayout(new GridLayout(16, 2, 4, 2));
		//#path
		add(path);
		add(ch0echo);
		add(pathName);
		add(ch1echo);
		//#instruments
		add(instruments);
		add(ch2echo);
		//#samples
		add(samples);
		add(ch3echo);
		add(sampleTypesMenu);
		add(ch4echo);
		//#SPC
		JLabel authorLabel = new JLabel("#author");
		add(authorLabel);
		add(ch5echo);
		add(author);
		add(ch6echo);
		JLabel titleLabel = new JLabel("#title");
		add(titleLabel);
		add(ch7echo);
		add(title);
		JLabel blank0 = new JLabel("");
		add(blank0);
		JLabel gameLabel = new JLabel("#game");
		add(gameLabel);
		JLabel blank1 = new JLabel("");
		add(blank1);
		add(game);
		JButton generate = new JButton("Create file");
		add(generate);
		generate.addActionListener(new gListener());
		JLabel commentLabel = new JLabel("#comment");
		add(commentLabel); 
		JLabel creditLabel = new JLabel("AutomusicK by MiniMacro 2019");
		add(creditLabel);
		add(comment);
		JLabel emailLabel = new JLabel("Contact: minimacrodx@gmail.com");
		add(emailLabel);
		//tempo commands
		JLabel tempoLabel = new JLabel("Enter song BPM or tempo command...");
		add(tempoLabel);
		JLabel blank2 = new JLabel("");
		add(blank2);
		add(tempo);
		add(successLabel);
	}
	public class gListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try 
			{
				//get states of echo checkboxes
				if (ch0echo.getState() == true)
					echoNum += 1;
				if (ch1echo.getState() == true)
					echoNum += 2;
				if (ch2echo.getState() == true)
					echoNum += 4;
				if (ch3echo.getState() == true)
					echoNum += 8;
				if (ch4echo.getState() == true)
					echoNum += 16;
				if (ch5echo.getState() == true)
					echoNum += 32;
				if (ch6echo.getState() == true)
					echoNum += 64;
				if (ch7echo.getState() == true)
					echoNum += 128;
				//convert echo to hex
				echoHex = Integer.toHexString(echoNum);
				echoHex = ("\n$EF$" + Integer.toHexString(echoNum) + "$00$00\n$F1$00$00$00");
				GenerateFile();
			}
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
		private void GenerateFile() throws IOException 
		{
			File file = new File("New_song.txt");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write("#amk 2");
			writer.write("\n" + echoHex + "\n");
			if (path.getState() == true)
			{
				writer.write("\n#path \"" + pathName.getText() + "\"");
			}
			if (samples.getState() == true)
			{
				if (sampleTypesMenu.getSelectedItem() != "none")
				{
					writer.write("\n#samples\n{\n\t" + sampleTypesMenu.getSelectedItem() + "\n}");
				}
				else
				{
					writer.write("\n#samples\n{\n\n}");
				}
			}
			if (instruments.getState() == true)
			{
				writer.write("\n#instruments\n{\n\n}");
			}
			writer.write("\n\n#SPC\n{\n\t#author  \"" + 
				author.getText() + "\"\n\t#title   \"" + 
				title.getText() + "\"\n\t#game    \"" + 
				game.getText() + "\"\n\t#comment \"" +
				comment.getText() + "\"\n}");
			if (tempo.getText().contains("t"))
			{
				writer.write("\n\n" + tempo.getText() + "\n");
			}
			else
			{
				double input = Double.parseDouble(tempo.getText());
				input *= 0.4096;
				int temp = (int)Math.ceil(input);
				writer.write("\n\nt" + temp + "\n");
			}
			writer.write("\n#0\n\n\n\n#1\n\n\n\n#2\n\n\n\n#3\n\n\n" + 
				"\n#4\n\n\n\n#5\n\n\n\n#6\n\n\n\n#7\n\n\n");
			writer.flush();
			writer.close();
			successLabel.setText("File creation successful.");
		}
	}
	public static void main(String[] args) throws IOException
	{
		JFrame frame = new JFrame("AutomusicK");
		frame.setSize(600, 600);
		frame.setLocation(100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new AutomusicK());
		frame.setVisible(true);
	}
}
