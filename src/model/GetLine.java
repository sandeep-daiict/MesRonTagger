package model;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class GetLine
{
    
    BufferedReader[] readersR;
    FileWriter fileWriter,fileWriter2;
    BufferedWriter fi2,fi3;
    
    public static void main(String[] args) throws IOException
    {
    	GetLine d = new GetLine();
        d.func();
    }
    
    public void func() throws IOException
    {
        File root = new File("/home/juhi/SEM II/major/dataset/Final/files");
        File[] list = root.listFiles();
        int num=list.length;
        File[] inFiles = new File[num];
        fileWriter = new FileWriter("out");
        fi2=new BufferedWriter(fileWriter);
        fileWriter2 = new FileWriter("ids");
        fi3=new BufferedWriter(fileWriter2);
    
    
    
        try {
            FileInputStream in;
            for (int i=0;i <num;i++) 
            {
                in = new FileInputStream(list[i]);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
    
                String line;
                while((line=br.readLine())!=null)
                {
                    fi2.write(line +" ");
                }
                fi2.write("\n");
                fi3.write(list[i].toString().split("/",2)[1]+"\n");
            }
            
        fi3.flush();
        fi2.flush();
        }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}