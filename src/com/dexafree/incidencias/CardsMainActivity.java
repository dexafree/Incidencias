package com.dexafree.incidencias;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.dexafree.incidencias.CardStack;
import com.dexafree.incidencias.CardUI;

/**
 * Created by Carlos on 20/05/13.
 */
public class CardsMainActivity extends SherlockActivity {

    private CardUI mCardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cards);

        // init CardView
        mCardView = (CardUI) findViewById(R.id.cardsview);
        mCardView.setSwipeable(true);

        // add AndroidViews Cards
        mCardView.addCard(new MyCard("Get the CardsUI view"));
        mCardView.addCardToLastStack(new MyCard("for Android at"));
        MyCard androidViewsCard = new MyCard("www.androidviews.net");
        androidViewsCard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.androidviews.net/"));
                startActivity(intent);

            }
        });
        mCardView.addCardToLastStack(androidViewsCard);

        // add one card, and then add another one to the last stack.
        mCardView.addCard(new MyCard("2 cards"));
        mCardView.addCardToLastStack(new MyCard("2 cards"));

        MyCard tarjeta = new MyCard("Esta es de prueba", "DESCRIPCION!");
        mCardView.addCardToLastStack(tarjeta);

        /*

        // add one card
        mCardView.addCard(new MyImageCard("Nexus 4 Part 1",R.drawable.url1));
        mCardView.addCardToLastStack(new MyImageCard("Nexus 4 Part 2",R.drawable.url2));
        mCardView.addCardToLastStack(new MyImageCard("Nexus 4 Part 3", R.drawable.url3));


        */



        // create a stack
        CardStack stack = new CardStack();
        stack.setTitle("title test");

        // add 3 cards to stack
        stack.add(new MyCard("3 cards", "DESCRIBIENDO"));
        stack.add(new MyCard("3 cards"));
        stack.add(new MyCard("3 cards"));

        // add stack to cardView
        mCardView.addStack(stack);

        // draw cards
        mCardView.refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}



