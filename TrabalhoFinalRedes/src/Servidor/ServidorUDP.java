/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Cliente.Arquivo;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author Alysson
 */
public class ServidorUDP extends Thread {
    
 
    public ServidorUDP() {
        this.start();
    }

    @Override
    public void run() {
        try {
            DatagramSocket ds = new DatagramSocket(30002);
            
            System.out.println("UDP: Aguardando Conexão . . .");
            while (true) {

                
                byte[] receiveData = new byte[1024*1024*5];
                
                while(true){
                    DatagramPacket pacote = new DatagramPacket(receiveData, receiveData.length);
                    
                    ds.receive(pacote);
                    
                    byte[] objectAsByte = pacote.getData();
                    
                    Arquivo arquivo = (Arquivo) getObjectFromByte(objectAsByte);
                    System.out.println("Escrevendo arquivo no diretorio");
                    String dir = arquivo.getDiretorioDestino().endsWith("/")
                        ? arquivo.getDiretorioDestino() + arquivo.getNome()
                        : arquivo.getDiretorioDestino() + "/" + arquivo.getNome();
                    System.out.println("Escrevendo arquivo em :" + dir);

                    FileOutputStream fos = new FileOutputStream(dir);
                    fos.write(arquivo.getConteudo());
                    fos.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    private Object getObjectFromByte(byte[] objectAsByte) {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(objectAsByte);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();

            bis.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
