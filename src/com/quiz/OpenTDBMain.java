package com.quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Command line quiz application that downloads quiz files from the <b>OpenTDB</b> site, 
 * displays them in console, and takes user input via a <code>Scanner</code>. 
 * <p>
 * Everything is in this one class for easy copying. Note that <code>JSON-Java</code> is required to run this application. 
 * It can be downloaded at <em>https://github.com/stleary/JSON-java</em>.
 * <p>
 * To run, simply execute this class. 
 * @author Mingrui Ma
 *
 */
public class OpenTDBMain {	
	/**
	 * 
	 * @return [0,1,2,3] in a random order.
	 */
	static int[] randomArray()	{
		ArrayList<Integer> remaining = new ArrayList<>();
		for(int i=1;i<=4;i++)
			remaining.add(i);
		Random random = new Random();
		
		int randomInt,
			idx = 0;
		int[] randomArr = new int[4];
		for(int i=4;i>0;i--)	{
			randomInt = random.nextInt(i);
			randomArr[idx++] = remaining.get(randomInt)-1;
			remaining.remove((Object) remaining.get(randomInt));
		}
		return randomArr;
	}
	
	/**
	 * 
	 * @param ar A String array with <em>exactly 4</em> elements.
	 * @return <code>ar</code> randomly scrambled.
	 */
	static String[] scrambleArray(String[] ar)	{
		int[] order = randomArray();
		String[] newArr = new String[4];
		for(int i=0;i<4;i++)	{
			newArr[order[i]] = ar[i];
		}
		return newArr;
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please configure the quiz.");
		System.out.println("");
		
		/*
		 * Configure question count. This will loop until an acceptable number is provided.
		 */
		int questionCount = 10;
		boolean illegalNumber = true;
		while(illegalNumber)	{
			try	{
				System.out.println("Input the number of questions to include and press [Enter]. Number must be between 1 and 50.");
				String input = scanner.next();
				questionCount = Integer.parseInt(input);
				if(questionCount<1 || questionCount>50)	
					System.out.println("!  Please input an integer between 1 and 50.");
				else	
					illegalNumber = false;
			} catch(NumberFormatException e)	{
				System.out.println("!  Illegal input.");	
			}
		}
		System.out.println("Question count set.");
		System.out.println();
		
		/*
		 * Configure category. This will loop until an acceptable category is provided.
		 */
		String category = "any";
		boolean illegalCategory = true;
		while(illegalCategory)	{
			try	{
				System.out.println("Input the category key and press [Enter]. Input \"keys\" to display the keys.");
				String input = scanner.next();
				while(input.equals("keys"))	{
					System.out.println("Key: [any] -- Any Category");
					System.out.println("Key: [9] -- General Knowledge");
					System.out.println("Key: [10] -- Entertainment: Books");
					System.out.println("Key: [11] -- Entertainment: Film");
					System.out.println("Key: [12] -- Entertainment: Music");
					System.out.println("Key: [13] -- Entertainment: Musicals and Theatres");
					System.out.println("Key: [14] -- Entertainment: Television");
					System.out.println("Key: [15] -- Entertainment: Video Games");
					System.out.println("Key: [16] -- Entertainment: Board Games");
					System.out.println("Key: [17] -- Science and Nature");
					System.out.println("Key: [18] -- Science: Computers");
					System.out.println("Key: [19] -- Science: Mathematics");
					System.out.println("Key: [20] -- Mythology");
					System.out.println("Key: [21] -- Sports");
					System.out.println("Key: [22] -- Geography");
					System.out.println("Key: [23] -- History");
					System.out.println("Key: [24] -- Politics");
					System.out.println("Key: [25] -- Art");
					System.out.println("Key: [26] -- Celebrities");
					System.out.println("Key: [27] -- Animals");
					System.out.println("Key: [28] -- Vehicles");
					System.out.println("Key: [29] -- Entertainment: Comics");
					System.out.println("Key: [30] -- Science: Gadgets");
					System.out.println("Key: [31] -- Entertainment: Japanese Anime and Manga");
					System.out.println("Key: [32] -- Entertainment: Cartoon and Animations");
					System.out.println();
					System.out.println("Input the category key. Input \"keys\" to display the keys.");
					input = scanner.next();
				}
				if(input.equals("any"))	{
					category = "any";
					illegalCategory = false;
				}
				else	{
					int categoryNum = Integer.parseInt(input);
					if(categoryNum >=9 && categoryNum <=32)	{
						category = input;
						illegalCategory = false;
					}
					else 	
						System.out.println("!  Illegal input.");
				}
			} catch(NumberFormatException e)	{
				System.out.println("!  Illegal input.");	
			}
		}
		System.out.println("Category set.");
		System.out.println();
		
		/*
		 * Configure difficulty. This will loop until an acceptable difficulty is provided.
		 */
		boolean illegalDifficulty = true;
		String difficulty = "any";
		while(illegalDifficulty)	{
			try	{
				System.out.println("Input the question difficulty and press [Enter]. Available difficulties: [any], [easy], [medium], [hard].");
				String input = scanner.next();
				if(input.equals("any") || input.equals("easy") || input.equals("medium") || input.equals("hard"))	{
					difficulty = input;
					illegalDifficulty = false;
				}
				else	{
					System.out.println("!  Illegal input.");
				}
			} catch(NumberFormatException e)	{
				System.out.println("!  Illegal input.");	
			}
		}
		System.out.println("Difficulty set.");
		System.out.println();
		
		/*
		 * Configure type. This will loop until an acceptable type is provided.
		 */
		boolean illegalType = true;
		String type = "any";
		while(illegalType)	{
			System.out.println("Input the question type and press [Enter]. Available types: [any], [multiple] (multiple choice), [boolean] (true/false).");
			String input = scanner.next();
			if(input.equals("any") || input.equals("multiple") || input.equals("boolean"))	{
				type = input;
				illegalType = false;
			}
			else	{
				System.out.println("!  Illegal input.");
			}
		}
		System.out.println("Type set.");
		System.out.println();
		
		String url = "https://opentdb.com/api.php?amount=" + questionCount;
		if(!category.equals("any"))
			url += "&category=" + category;
		if(!difficulty.equals("any"))
			url += "&difficulty=" + difficulty;
		if(!type.equals("any"))
			url += "&type=" + type;

		System.out.println("Quiz configured. Now downloading.");
		for(int i=0;i<8;i++)
			System.out.println();
		
		/*
		 * Download the JSON.
		 */
		JSONObject jsonOb = new JSONObject();
		try(InputStream is = new URL(url).openStream())	{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonStr = "";
			int next = reader.read();
			while(next != -1)	{
				jsonStr += (char) next;
				next=reader.read();
			}
			jsonOb = new JSONObject(jsonStr);
		}	catch(IOException e)	{
			e.printStackTrace();
		}	catch(JSONException e)	{
			e.printStackTrace();
		}
		
		JSONArray results = jsonOb.getJSONArray("results");
		int corrects = 0;
		for(int i=0;i<results.length();i++)	{
			JSONObject arrayElement = results.getJSONObject(i);
			/*
			 * print the question, category and difficulty information.
			 */
			int q = i+1;
			StringBuilder questionStr = new StringBuilder(arrayElement.getString("question"));
			//replace HTML double quotation mark with \"
			int doubleQuotationLocation = questionStr.indexOf("&quot;");
			while(doubleQuotationLocation != -1)	{
				questionStr.replace(doubleQuotationLocation, doubleQuotationLocation+6, "\"");
				doubleQuotationLocation = questionStr.indexOf("&quot;");
			}
			//replace HTML single quotation mark with \'
			int singleQuotationLocation = questionStr.indexOf("&#039;");
			while(singleQuotationLocation != -1)	{
				questionStr.replace(singleQuotationLocation, singleQuotationLocation+6, "\'");
				singleQuotationLocation = questionStr.indexOf("&#039;");
			}
			System.out.println("Category: " + arrayElement.getString("category") + " | Difficulty: " + arrayElement.getString("difficulty"));
			System.out.print("Question " + q + ": ");
			System.out.println(questionStr.toString());

			String[] answers,
			answersScrambled = new String[0];
			String correctAnswer = "";
			/*
			 * print the answers if question is multiple choice.
			 */
			if(arrayElement.getString("type").equals("multiple"))	{
				answers = new String[4];
				correctAnswer = arrayElement.getString("correct_answer");
				answers[0] = correctAnswer;	
				JSONArray incorrectAnswers = arrayElement.getJSONArray("incorrect_answers");
				for(int j=0;j<=2;j++)	{
					int idx = j+1;
					answers[idx] = incorrectAnswers.getString(j);
				}
				//now answers is [correct, incorrect, incorrect, incorrect]; now randomize order.
				answersScrambled = scrambleArray(answers);
				for(int j=0;j<=3;j++)	{
					int option = j+1;
					System.out.println("Answer " + option + ": " + answersScrambled[j]);
				}
				/*
				 * Choose an answer. This will loop until an acceptable answer is provided.
				 */
				boolean illegalAnswer = true;
				int chosenAnswer = 1;
				while(illegalAnswer)	{
					try	{
						System.out.println("(Select answer by number)");
						String input = scanner.next();
						chosenAnswer = Integer.parseInt(input);
						if(chosenAnswer<1 || chosenAnswer>4)	
							System.out.println("!  Please input a number between 1 and 4.");
						else	
							illegalAnswer= false;
					} catch(NumberFormatException e)	{
						System.out.println("!  Illegal input.");	
					}
				}
				if(correctAnswer.equals(answersScrambled[chosenAnswer-1]))	{
					corrects++;
					System.out.println("!  Correct!");
				}
				else	{
					System.out.println("!  Sorry wrong answer.");
				}
				System.out.println();
			}

			/*
			 * Print the answers if question is true/false.
			 */
			if(arrayElement.getString("type").equals("boolean"))	{
				answers = new String[2];
				System.out.println("Answer 1: True");
				System.out.println("Answer 2: False");
				correctAnswer = arrayElement.getString("correct_answer");
				/*
				 * Choose an answer. This will loop until an acceptable answer is provided.
				 */
				boolean illegalAnswer = true;
				int chosenAnswer = 1;
				while(illegalAnswer)	{
					try	{
						System.out.println("(Select answer by number)");
						String input = scanner.next();
						chosenAnswer = Integer.parseInt(input);
						if(chosenAnswer<1 || chosenAnswer>2)	
							System.out.println("!  Please input a number between 1 and 2.");
						else	
							illegalAnswer= false;
					} catch(NumberFormatException e)	{
						System.out.println("!  Illegal input.");	
					}
				}
				String chosenAnswerContent = (chosenAnswer==1) ? "True" : "False";
				if(correctAnswer.equals(chosenAnswerContent))	{
					corrects++;
					System.out.println("!  Correct!");
				}
				else	{
					System.out.println("!  Sorry wrong answer.");
				}
				System.out.println();
			}

		}

		for(int i=0;i<5;i++)
			System.out.println();
		System.out.println("Quiz completed. Your score is: " + corrects + "/" + questionCount + ".");
		
		scanner.close();
	}
}
