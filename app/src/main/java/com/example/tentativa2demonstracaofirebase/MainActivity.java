package com.example.tentativa2demonstracaofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Referência para o banco de dados no Firebase
    //Referência que "aponta" para o nó da raiz da árvore.

    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();



    //atributos dos objetos da interface gráfica
    private EditText txtNome;
    private EditText txtSobrenome;
    private EditText txtIdade;
    private Button btnEnviar;
    private Button btnListar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ligando os atributos com os objetos da interface gráfica

        txtNome = findViewById( R.id.txtNome );
        txtSobrenome = findViewById( R.id.txtSobrenome );
        txtIdade = findViewById( R.id.txtIdade );
        btnEnviar = findViewById( R.id.btnEnviar );
        btnListar = findViewById( R.id.btnListar );

        //Escutador do botão ENVIAR

        btnEnviar.setOnClickListener(new EscutadorBotaoEnviar());

        //Escutador do botão LISTAR

        btnListar.setOnClickListener(new EscutadorBotaoListar());




    }

    public class EscutadorBotaoEnviar implements View.OnClickListener{
        @Override
        public void onClick(View view){
            //Variáveis para captura de dados
            String nome, sobrenome;
            int idade;

            //Atribuindo valores à variáveis pelos dados nas caixas de texto
            nome = txtNome.getText().toString();
            sobrenome = txtSobrenome.getText().toString();
            idade = Integer.parseInt(txtIdade.getText().toString());


            //Criando um objeto usuário u da classe Usuario
            Usuario u = new Usuario(nome, sobrenome, idade);

            //Referência para o nó principal da nossa tabela a aprtir de seu filho
            DatabaseReference bancoFilho = BD.child("bancoFilho");

            //Neste caso, estaremos gerando um nó aleatório, semelhante a um auto_increment no SQL
            // O método push() gerará um valor aleatório
            // O método getKey() devolve esse valor para ser utilizado

            String chave = bancoFilho.push().getKey();

            bancoFilho.child(chave).setValue(u);



        }

    }

    //Agora será necessário criar uma classe interna do Firebase

    private class EscutadorFirebase implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            //Verificando se os dados estão no DataSnapshot

            if ( dataSnapshot.exists() ) {

                //Algumas variáveis para guardar dados vindos da consulta
                String nome, sobrenome;
                int idade;

                //Percorrendo o Snapshot e mostrando por meio de um toast

                for ( DataSnapshot datasnapUsuario : dataSnapshot.getChildren() ) {

                    // A cada usuário do datasnapshot guarda ele em um objeto da classe
                    Usuario u = datasnapUsuario.getValue( Usuario.class );

                    //Atribuindo os valores às variáveis
                    nome = u.getNome();
                    sobrenome = u.getSobrenome();
                    idade = u.getIdade();

                    //Exibindo no Toast
                    Toast.makeText(MainActivity.this, "Nome: "+nome+"\nSobrenome: "+sobrenome+"\nIdade: "+idade, Toast.LENGTH_SHORT).show();
                }
            }

        }

        // Não usado...

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    // Classe interna do escutador do botão LISTAR:

    private class EscutadorBotaoListar implements View.OnClickListener {

        @Override
        public void onClick(View view) {


            DatabaseReference bancoFilho = BD.child( "bancoFilho" );


          bancoFilho.addValueEventListener( new EscutadorFirebase() );
        }
    }
    }



