package com.dexafree.incidencias;

import java.io.InputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RssParserDom
{
    private URL rssUrl;

    public RssParserDom(String url)
    {
        try
        {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<Favoritos> parse()
    //public List<Favoritos> parse()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //List<Favoritos> favoritos = new ArrayList<Favoritos>();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(this.getInputStream());
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("favorito");

            for (int i=0; i<items.getLength(); i++)
            {
                Favoritos favorito = new Favoritos();

                Node item = items.item(i);
                NodeList datosNoticia = item.getChildNodes();

                for (int j=0; j<datosNoticia.getLength(); j++)
                {
                    Node dato = datosNoticia.item(j);
                    String etiqueta = dato.getNodeName();

                    if (etiqueta.equals("carretera"))
                    {
                        String texto = obtenerTexto(dato);

                        favorito.setCarretera(texto);
                    }
                    else if (etiqueta.equals("pkInicial"))
                    {
                        String texto = obtenerTexto(dato);

                        int pkI = Integer.parseInt(texto);

                        favorito.setPkInicial(pkI);
                    }
                    else if (etiqueta.equals("pkFinal"))
                    {
                        String texto = obtenerTexto(dato);

                        int pkF = Integer.parseInt(texto);

                        favorito.setPkFinal(pkF);


                    }

                }

                Favoritos.FavoritosList.add(favorito);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return Favoritos.FavoritosList;
    }

    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }

        return texto.toString();
    }

    private InputStream getInputStream()
    {
        try
        {
            return rssUrl.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}