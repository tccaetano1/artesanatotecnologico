package com.seupacote;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    EditText editIp, editPorta;
    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editIp = findViewById(R.id.editIp);
        editPorta = findViewById(R.id.editPorta);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(v -> {
            String ip = editIp.getText().toString().trim();
            String portaStr = editPorta.getText().toString().trim();

            if (ip.isEmpty() || portaStr.isEmpty()) {
                Toast.makeText(this, "Preencha IP e Porta", Toast.LENGTH_SHORT).show();
                return;
            }

            int porta;
            try {
                porta = Integer.parseInt(portaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Porta inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarMensagemUdp(ip, porta, "start");
        });
    }

    private void enviarMensagemUdp(String ip, int porta, String mensagem) {
        new Thread(() -> {
            try {
                InetAddress endereco = InetAddress.getByName(ip);
                byte[] dados = mensagem.getBytes();

                DatagramPacket pacote = new DatagramPacket(dados, dados.length, endereco, porta);
                DatagramSocket socket = new DatagramSocket();
                socket.send(pacote);
                socket.close();

                runOnUiThread(() -> Toast.makeText(this, "Enviado!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}