import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiClient extends Application{
	Label welcomeMessage, portNumMessage, IPAddrMessage, ErrorMessage, ErrDealMessage,
			play_wager,ante_wager,pair_plus,dealer_card_message, user_card_message,
			clientCardText, dealerCardText, how_much_won, try_again, total_Winnings;
	TextField portNumInput, IPAddrInput, c1,
				play_wager_input, ante_wager_input, pair_plus_input;
	Button b1, connectToServer, deal,play, fold, next, try_yes, try_no;
	HashMap<String, Scene> sceneMap;
	Client clientConnection;
	ListView<String> listItems2;
	MenuBar menu;
	Menu options,rules;
	MenuItem newLook, freshStart, exit;
	PokerInfo pokerInf;
	List<ImageView> dealerCards;
	List<ImageView> clientCards;
	MenuItem rulesMenu, payouts;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");

		setUp();

		setOnAction(primaryStage);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

		primaryStage.setScene(this.sceneMap.get("start"));
		primaryStage.show();
		
	}

	public void setUp(){
		this.pokerInf = new PokerInfo();

		//play scene utilities
			//menu bar set up
		this.menu = new MenuBar();
		this.options = new Menu("Options");
		this.rules = new Menu("Rules");
		this.payouts = new Menu("Payouts");
		this.newLook	= new MenuItem("New Look");
		this.freshStart = new MenuItem("Fresh Start");
		this.rulesMenu = new MenuItem("Rules");
		this.exit = new MenuItem("Exit");
		this.options.getItems().addAll(newLook,freshStart,exit);
		this.rules.getItems().addAll(rulesMenu, payouts);
		this.menu.getMenus().addAll(options,rules);
			//Label texts
		this.play_wager = new Label("Play Wager:");
		this.play_wager.setStyle("-fx-font: bold 15 Monaco;" + "-fx-text-fill: LIGHTGRAY;");

		this.ante_wager = new Label("Ante Wager:");
		this.ante_wager.setStyle("-fx-font: bold 15 Monaco;" + "-fx-text-fill: LIGHTGRAY;");

		this.pair_plus = new Label( "Pair Plus :");
		this.pair_plus.setStyle("-fx-font: bold 15 Monaco;" + "-fx-text-fill: LIGHTGRAY;");

		this.dealer_card_message = new Label("Dealer's Cards");
		this.user_card_message = new Label("Your Cards");

		this.ErrDealMessage = new Label(">Please check values for the wagers\n" +
											"Remember values must be between $5-$25");
		this.clientCardText = new Label("Client's Cards will show here");
		this.dealerCardText = new Label("Dealer's cards will show here");
		this.clientCardText.setStyle("-fx-font: bold 18 Monaco;" + "-fx-text-fill: LIGHTGRAY;");
		this.dealerCardText.setStyle("-fx-font: bold 18 Monaco;" + "-fx-text-fill: LIGHTGRAY;");

			//text fields
		this.play_wager_input = new TextField();
		play_wager_input.setPromptText("$5-$25");
		play_wager_input.setStyle("-fx-background-color: -fx-control-inner-background;");
		this.ante_wager_input =  new TextField();
		ante_wager_input.setPromptText("$5-$25");
		ante_wager_input.setStyle("-fx-background-color: -fx-control-inner-background;");
		this.pair_plus_input =  new TextField();
		pair_plus_input.setPromptText("$5-$25");
		pair_plus_input.setStyle("-fx-background-color: -fx-control-inner-background;");
			//Buttons
		this.play = new Button("Play");
		this.play.setDisable(true);
		this.fold = new Button("Fold");
		this.fold.setDisable(true);
		this.deal = new Button("Deal");
		this.next = new Button("Next");
		this.next.setDisable(true);

		this.play.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");
		this.fold.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");
		this.deal.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");
		this.next.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");

		//end of scene
		this.total_Winnings = new Label();
		this.how_much_won = new Label();
		this.try_again = new Label("Do you want to try again?");
		this.try_again.setStyle("-fx-font: bold 18 Monaco;");
		this.try_yes = new Button("YES");
		this.try_no = new Button("NO");
		this.try_yes.setPrefWidth(80);
		this.try_yes.setPrefHeight(50);
		this.try_yes.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");
		this.try_no.setPrefWidth(80);
		this.try_no.setPrefHeight(50);
		this.try_no.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");


		//Client GUI
		this.c1 = new TextField();
		this.b1 = new Button("Send");

		//start scene utilities
		this.ErrorMessage = new Label("CONNECTION FAILED\nPlease check port number and IP Address");
		this.ErrorMessage.setStyle("-fx-font: bold 18 Monaco;" + "-fx-text-fill: RED;");

		this.welcomeMessage = new Label(" Welcome to\n3 Card Poker\n\n\n");
		this.welcomeMessage.setStyle("-fx-font: bold 36 Monaco;");

		this.portNumMessage = new Label("Enter Port Number:");
		this.portNumMessage.setStyle("-fx-font: bold 15 Monaco;");

		this.IPAddrMessage = new Label("Enter IP Address:");
		this.IPAddrMessage.setStyle("-fx-font: bold 15 Monaco;");

		this.portNumInput = new TextField("5555");
		this.portNumInput.setStyle("-fx-background-color: -fx-control-inner-background;"+
				"-fx-font:  12 Monaco;");

		this.IPAddrInput =  new TextField("127.0.0.1");
		this.IPAddrInput.setStyle("-fx-background-color: -fx-control-inner-background;"+
				"-fx-font:  12 Monaco;");

		this.connectToServer = new Button("CONNECT");
		this.connectToServer.setPrefWidth(100);
		this.connectToServer.setPrefHeight(50);
		this.connectToServer.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");

		//list view data
		this.listItems2 = new ListView<String>();

		//create hash map for scenes
		this.sceneMap = new HashMap<String, Scene>();
		this.sceneMap.put("start", createStartScene());
		this.sceneMap.put("play", createPlayScene());
		this.sceneMap.put("end", createWinningsScene());
	}

	public void setOnAction(Stage primaryStage){

		this.connectToServer.setOnAction( e -> {

			if(!portNumInput.getText().matches("[0-9]+")){
				ErrorMessage.setVisible(true);
			}
			else if(!IPAddrInput.getText().matches(
					"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")){

					ErrorMessage.setVisible(true);
			}
			else{
				ErrorMessage.setVisible(false);
				primaryStage.setScene(sceneMap.get("play"));
				primaryStage.setTitle("This is a client");
				clientConnection = new Client(data->{
						Platform.runLater(()->{
							PokerInfo temp = (PokerInfo) data;
							System.out.println("Client received the info and is in step: " + temp.counter );
							if(temp.getCounter() == 0) {
								pokerInf = temp;
							}
							if(temp.getCounter() == 1){
								System.out.println("Client received the cards");
								pokerInf = temp;
								clientCardText.setText(temp.getClientCards());
								List<Image> cardImages = getClientCardImages(temp);
								for (int i = 0; i < clientCards.size(); i++) {
									ImageView view = clientCards.get(i);
									view.setImage(cardImages.get(i));
								}
							}
							else if(temp.getCounter() == 2){
								System.out.println("Client received confirmation that the bet was set");
								pokerInf = temp;
								dealerCardText.setText(temp.getDealerCards());
								List<Image> cardImages = getDealerCardImages(temp);
								for (int i = 0; i < dealerCards.size(); i++) {
									ImageView view = dealerCards.get(i);
									view.setImage(cardImages.get(i));
								}
							}
							else if(temp.getCounter() == 3){
								pokerInf = temp;
								System.out.println("Client is in the end off the scene");
								how_much_won.setText(pokerInf.win_or_loose_message + "\nThis is how much you won : " + temp.winning);
								how_much_won.setStyle("-fx-font: bold 24 Monaco;");
								total_Winnings.setText("Total winnning: $" + temp.total_winnings);
								total_Winnings.setStyle("-fx-font: bold 24 Monaco;");
							}
							else if(temp.getCounter() == -1){
								System.out.println("Attempting to receive a message ffrom another client");
								listItems2.getItems().add(temp.getMessage());
							}
						});

				}, IPAddrInput.getText(), portNumInput.getText());

				clientConnection.start();
			}
		});

		this.freshStart.setOnAction(e->{
			primaryStage.setScene(createPlayScene());

			this.pair_plus_input.clear();
			this.play_wager_input.clear();
			this.ante_wager_input.clear();

			this.pair_plus_input.setEditable(true);
			this.play_wager_input.setEditable(true);
			this.ante_wager_input.setEditable(true);

			this.play.setDisable(true);
			this.fold.setDisable(true);
			this.next.setDisable(true);
			this.deal.setDisable(false);

			this.pokerInf.setMessage("Client is chose fresh start");
			pokerInf.setCounter(-2);
			pokerInf.reset();
			this.clientConnection.send(pokerInf);
			System.out.println("Client is going to try again");
		});

		this.deal.setOnAction( e -> {
			try {

				int anteWage = Integer.parseInt(ante_wager_input.getText());

				int pairWage = 0;
				if(pair_plus_input.getText().isEmpty()){
					pairWage = 0;
				} else {
					pairWage = Integer.parseInt(pair_plus_input.getText());
				}

				System.out.println(anteWage + " " + pairWage);

				if ((anteWage > 25) || (anteWage < 5)){
					this.ErrDealMessage.setVisible(true);
				} else if (((pairWage > 25) && (pairWage != 0)) || ((pairWage < 5)&&( pairWage != 0))) {
					this.ErrDealMessage.setVisible(true);
				} else {
					this.ErrDealMessage.setVisible(false);

					this.pokerInf.setAnte_wager(anteWage);
					this.pokerInf.setPair_wager(pairWage);
					this.pokerInf.setMessage("Player" + pokerInf.getClient_number() + "started a game!");

					this.ante_wager_input.setEditable(false);
					this.pair_plus_input.setEditable(false);
					deal.setDisable(true);
					this.play.setDisable(false);
					this.fold.setDisable(false);

					this.pokerInf.setMessage("Just started a game");
					this.clientConnection.send(pokerInf);
				}

			} catch(NumberFormatException event){
				ErrDealMessage.setVisible(true);
			}
		});

		this.play.setOnAction( e-> {
			System.out.println("user just pressed play");
			int anteWage = Integer.parseInt(ante_wager_input.getText());
			try{
				System.out.println("trying to send data");
				int playWage = Integer.parseInt(play_wager_input.getText());
				if ((playWage > 25) || (playWage < anteWage)) {
					ErrDealMessage.setVisible(true);
				}
				else{
					System.out.println("we are in the send data code ");
					this.ErrDealMessage.setVisible(false);
					this.play.setDisable(true);
					this.next.setDisable(false);
					this.fold.setDisable(true);
					this.play_wager_input.setEditable(false);
					Integer tot = pokerInf.getPlay_wager() + pokerInf.getAnte_wager() + pokerInf.getPair_wager();
					this.pokerInf.setMessage("Client made a bet of: " + tot);
					this.pokerInf.setPlay_wager(playWage);
					this.clientConnection.send(pokerInf);
					System.out.println("poker wager got sent");
				}
			}
			catch(NumberFormatException event){
				ErrDealMessage.setVisible(true);
			}
		});

		this.fold.setOnAction( e-> {
			System.out.println("user just pressed fold");
			System.out.println("trying to send fold data");
			this.ErrDealMessage.setVisible(false);
			this.play.setDisable(true);
			this.next.setDisable(false);
			this.fold.setDisable(true);
			this.pokerInf.setMessage("Client just folded");
			this.clientConnection.send(pokerInf);
			System.out.println("poker info got sent");
		});

		this.next.setOnAction( e-> {
			primaryStage.setScene(sceneMap.get("end"));
			primaryStage.setTitle("Winnings Scene");
			this.pokerInf.setMessage("Client finished game." +
					"\nDealer's hand: " + pokerInf.dealerHand +
					"\nclient's hand is: " + pokerInf.clientHand +
					"\n" + pokerInf.win_or_loose_message);
			this.clientConnection.send(pokerInf);
			System.out.println("Client just send info for the last scene");
		});

		this.try_yes.setOnAction(e -> {
			//reset scene
			primaryStage.setScene(createPlayScene());

			this.pair_plus_input.clear();
			this.play_wager_input.clear();
			this.ante_wager_input.clear();

			this.pair_plus_input.setEditable(true);
			this.play_wager_input.setEditable(true);
			this.ante_wager_input.setEditable(true);

			this.play.setDisable(true);
			this.fold.setDisable(true);
			this.next.setDisable(true);
			this.deal.setDisable(false);

			this.dealerCardText.setText("Dealer's Cards will show here");
			this.clientCardText.setText("Client's Cards will show here");

			this.pokerInf.setMessage("Client is going to play another game, wish them luck");
			pokerInf.setCounter(-2);
			this.clientConnection.send(pokerInf);
			System.out.println("Client is going to try again");
		});

		this.try_no.setOnAction(e -> {
			Platform.exit();
		});

		this.rules.setOnAction(e -> {
			BorderPane bp= new BorderPane();
			bp.setCenter(rulestxt());
			bp.setStyle("-fx-base: #83cf6b; ");
			Scene rulesScene = new Scene(bp, 700,700);
			Stage popupwindow = new Stage();
			popupwindow.setScene(rulesScene);
			popupwindow.show();
		});

		this.payouts.setOnAction(e -> {
			BorderPane bp= new BorderPane();
			bp.setCenter(payoutstxt());
			bp.setStyle("-fx-base: #83cf6b; ");
			Scene payoutsScene = new Scene(bp, 700,700);
			Stage popupwindow = new Stage();
			popupwindow.setScene(payoutsScene);
			popupwindow.show();
		});
	}

	public Text rulestxt(){
		String title = "3 Pocker Game:\n\n";
		Text rulesTX = new Text(title + "Place an Ante wager and/or a pair plus wager, betting\n " +
				"that they will have a hand of at least a pair or better.\n" +
				"Three cards are then dealt the player and to the dealer.\n" +
				"Look at your hand and determine to place a play wager\n" +
				"(equal to the amount of the ante wager) to pit the hand\n" +
				"against the dealer’s hand or not. Optimum strategy says\n" +
				"the player should “play all hands greater than Queen,”\n" +
				"Six and Four and fold all hands worse. If you fold,\n" +
				"the hand is over and the dealer will collect your ante\n" +
				"wager and pair plus wager. If you place a play wager,\n" +
				"the cards will be turned over to determine if you have\n" +
				"a better hand than the dealer. If the dealer has a hand\n" +
				"of Jack-high or worse, the play wager is returned to you.\n" +
				"If the dealer has a hand of Queen-high or better, both the\n" +
				"play wager and the ante are paid out at 1 to 1 if the player\n" +
				"has a better hand than the dealer. If the dealer’s hand is\n" +
				"superior, both the ante and play bets are collected. The pair\n" +
				"plus bet is determined completely independent to what the\ndealer has.");
		rulesTX.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");


		return rulesTX;
	}

	public Text payoutstxt(){
		String title = "3 Pocker Game Payouts:\n\n";
		Text rulesTX = new Text(title + "Pair plus payouts:\n" +
				"\n" +
				"Straight Flush                    40 to 1\n" +
				"\n" +
				"Three of a Kind                   30 to 1\n" +
				"\n" +
				"Straight                          6 to 1\n" +
				"\n" +
				"Flush                             3 to 1\n" +
				"\n" +
				"Pair                              1 to 1\n" +
				"\n" +
				"\n\nAnte bonus payouts:\n" +
				"\n" +
				"Straight Flush                    5 to 1\n" +
				"\n" +
				"Three of a Kind                   4 to 1\n" +
				"\n" +
				"Straight                          1 to 1.\n\n\nSource: https://www.caesars.com/casino-gaming-blog/latest-posts/poker");
		rulesTX.setStyle("-fx-font: normal 16 Monaco; "
				+ "-fx-base: #3d3d3d; "
				+ "-fx-text-fill: #7bde54;");


		return rulesTX;
	}

	public Scene createStartScene(){
		welcomeMessage.setAlignment(Pos.CENTER);
		ErrorMessage.setVisible(false);

		VBox V1 = new VBox(30,portNumMessage, portNumInput);
		VBox V2 = new VBox(30, IPAddrMessage,IPAddrInput);
		HBox H1 = new HBox(30, V1, V2);
		H1.setAlignment(Pos.CENTER);
		VBox VB = new VBox(30, welcomeMessage, H1, connectToServer, ErrorMessage);
		VB.setAlignment(Pos.CENTER);
		VB.setStyle("-fx-base: #3e941c; ");
		return new Scene(VB,500, 500);
	}

	public Scene createPlayScene() {
		this.dealerCards = new ArrayList<>();
		this.clientCards = new ArrayList<>();
		for (int i = 0; i < 3; i++){
			ImageView dealerCardImg = new ImageView(getCardBack());
			dealerCardImg.setPreserveRatio(true);
			dealerCardImg.setFitWidth(100);
			this.dealerCards.add(dealerCardImg);

			ImageView clientCardImg = new ImageView(getCardBack());
			clientCardImg.setPreserveRatio(true);
			clientCardImg.setFitWidth(100);
			this.clientCards.add(clientCardImg);
		}

		ErrDealMessage.setVisible(false);

		BorderPane bp = new BorderPane();

		HBox h1 = new HBox(play_wager, play_wager_input);
 		HBox h2 = new HBox(ante_wager, ante_wager_input);
		HBox h3 =  new HBox(pair_plus, pair_plus_input);
		HBox h4 = new HBox(30, play,fold, next);
		h4.setAlignment(Pos.CENTER);

		VBox v1 = new VBox(20, h1,h2,h3,deal,ErrDealMessage);
		v1.setAlignment(Pos.CENTER);
//		VBox clientBox = new VBox(10, c1,b1,listItems2);
		VBox clientBox = new VBox(listItems2);
		clientBox.setAlignment(Pos.CENTER);

		clientBox.setStyle("-fx-background-color:#3e941c; "+ "-fx-base: #3e941c; ");


		dealerCardText.setAlignment(Pos.TOP_CENTER);
		clientCardText.setAlignment(Pos.BASELINE_CENTER);
		//VBox v2 =  new VBox(300, dealerCardText, clientCardText);
		HBox dealerCardImages = new HBox(dealerCards.get(0),
				dealerCards.get(1), dealerCards.get(2));
		HBox clientCardImages = new HBox(clientCards.get(0),
				clientCards.get(1), clientCards.get(2));
		dealerCardImages.setAlignment(Pos.TOP_CENTER);
		clientCardImages.setAlignment(Pos.BASELINE_CENTER);
		VBox v2 = new VBox(150, dealerCardImages, clientCardImages);
		v2.setAlignment(Pos.CENTER);

		bp.setCenter(v2);
		bp.setTop(menu);
		bp.setLeft(clientBox);
		bp.setRight(v1);
		bp.setBottom(h4);
		h4.setStyle("-fx-background-color: #4f4f4e");

		Image pocker = new Image("file:src/main/resources/pictures/green.jpg");
		BackgroundImage pockerBack = new BackgroundImage(pocker, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background pockerB = new Background(pockerBack);

		Image pockerBlue = new Image("file:src/main/resources/pictures/blue1.jpg");
		BackgroundImage pockerBlueBack = new BackgroundImage(pockerBlue, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background pockerBlueB = new Background(pockerBlueBack);
		bp.setBackground(pockerB);

		final Boolean[] newLookChange = new Boolean[1];
		newLookChange[0] = false;
		this.newLook.setOnAction(e->{
			if(!newLookChange[0]) {
				bp.setBackground(pockerBlueB);
				newLookChange[0] = true;
				this.play_wager.setStyle("-fx-font: bold 15 Monaco;" +"-fx-text-fill: #333332;");
				this.ante_wager.setStyle("-fx-font: bold 15 Monaco;" +"-fx-text-fill:  #333332;");
				this.pair_plus.setStyle("-fx-font: bold 15 Monaco;" +"-fx-text-fill:  #333332;");
				this.clientCardText.setStyle("-fx-font: bold 18 Monaco;" +"-fx-text-fill:  #333332;");
				this.dealerCardText.setStyle("-fx-font: bold 18 Monaco;" +"-fx-text-fill:  #333332;");
				clientBox.setStyle("-fx-background-color:#9dc5eb; "+ "-fx-base: #9dc5eb; ");


			}
			else{
				bp.setBackground(pockerB);
				newLookChange[0] = false;
				this.play_wager.setStyle("-fx-font: bold 15 Monaco;" +"-fx-text-fill: LIGHTGRAY;");
				this.ante_wager.setStyle("-fx-font: bold 15 Monaco;" +"-fx-text-fill: LIGHTGRAY;");
				this.pair_plus.setStyle("-fx-font: bold 15 Monaco;" +"-fx-text-fill: LIGHTGRAY;");
				this.clientCardText.setStyle("-fx-font: bold 18 Monaco;" +"-fx-text-fill: LIGHTGRAY;");
				this.dealerCardText.setStyle("-fx-font: bold 18 Monaco;" +"-fx-text-fill: LIGHTGRAY;");
				clientBox.setStyle("-fx-background-color:#3e941c; "+ "-fx-base: #3e941c; ");
			}
		});

		return new Scene(bp, 1000, 600);
	}

	public Scene createWinningsScene(){
		HBox box = new HBox(30,try_yes,try_no);

		VBox vb = new VBox(30,how_much_won, try_again, box, total_Winnings);
		vb.setAlignment(Pos.CENTER);
		box.setAlignment(Pos.CENTER);

		vb.setStyle("-fx-base: #3e941c; ");

		return new Scene(vb, 500,500);
	}
	List<Image> getDealerCardImages(PokerInfo pokerInfo) {
		List<Image> images = new ArrayList<>();
		for(Card card : pokerInfo.dealerCards){
			images.add(getCardFront(card));
		}
		return images;
	}

	List<Image> getClientCardImages(PokerInfo pokerInfo) {
		List<Image> images = new ArrayList<>();
		for(Card card : pokerInfo.clientCards){
			images.add(getCardFront(card));
		}
		return images;
	}

	public Image getCardFront(Card card) {
		Image front = new Image("file:src/main/resources/Deck/" + card.getSuit() + "_" + card.getRank() +".png");
		return front;
	}

	public Image getCardBack() {
		Image back = new Image("file:src/main/resources/Back/blue.png");
		return back;
	}
}
