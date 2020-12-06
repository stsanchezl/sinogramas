/**
 * Undocumented methods are getters and setters
 */
package logic.sinogramas;

/**
 * This class is meant to manage the data within the data structures.
 * @author Cristian Davil Camilo Santos Gil
 * @author Diego Esteban Quintero Rey
 * @author Kevin Jair Gonzalez Sanchez
 * @author Stiven Leonardo Sánchez León 
 * @version 5.0
 * @since 16/10/2020
 */

import data.sinogramas.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Float.parseFloat;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Archive {

    // private boolean needSort = false;
    // private boolean needHeapSort = false;
    // private String dataStructure; //data structure to use.
    // private String regex = "[U].[\\dA-F]{4,5}"; //Regular expression to find info in the given text
    public BufferedReader text;  //Representation of the text in memory
    // private InputStream textToParse;

    // Llevan registro de cuales árboles se han creado en los arreglos de BSTs
    private int[] strokesList;
    private int[] favStrokesList;
    private int[] radixesList;
    private int[] favRadixesList;
    
    private AVLTreeGeneric<Unihan> tempAVL; // AVL principal o general
    
    //Arreglos de árboles BST que aplican en los filtros
    private BSTRefGeneric<Unihan>[] bstStrokesArray;
    private BSTRefGeneric<Unihan>[] favBSTStrokesArray;
    private BSTRefGeneric<Unihan>[] bstRadixesArray;
    private BSTRefGeneric<Unihan>[] favBSTRadixesArray;
    
    // Árbol auxiliar usado para instanciar los BST que se almacenan en los anteriores arreglos
    private BSTRefGeneric<Unihan> tempBST;
    
    private AVLTreeGeneric<Unihan> favAVL; // AVL de favoritos
    private ArrayList<SpanishDef> esDefArray; // Arreglo que guarda los SpanishDef
    private RabinKarp rabinKarp;

    /**
     * Class constructor, it initializes a specific linear data structure so they can hold the
     * Han characters, it does not matter the implementation of the data structure
     * @param arrayOrReferences: Implementation of the array
     * @param dataStructure: Data structure to be used.
     * @param ordered: whether the structure is ordered or not
     * @param textToParse: InputStream with the file so it can be put onto a BufferReader
     */
    
    public Archive() {
        this.strokesList = new int[59];
        this.favStrokesList = new int[59];
        this.radixesList = new int[215];
        this.favRadixesList = new int[215];
        this.bstRadixesArray = new BSTRefGeneric[215];
        this.favBSTRadixesArray = new BSTRefGeneric[215];
        this.bstStrokesArray = new BSTRefGeneric[59];
        this.favBSTStrokesArray = new BSTRefGeneric[59];
        this.tempAVL = new AVLTreeGeneric<>();
        this.favAVL = new AVLTreeGeneric<>();
        this.esDefArray = new ArrayList<>();
    }
    
    
    
    public Archive(String arrayOrReferences, String dataStructure, String ordered, InputStream textToParse) {
        
        /*
        this.dataStructure = dataStructure;
        this.textToParse = textToParse;
        switch (dataStructure) {
            case "l":
                if (ordered.equals("u")) {
                    needSort = true;
                    if (arrayOrReferences.equals("a")) {
                        this.tempList = new UnorderedListArrayGeneric<>(1500000);
                    } else {
                        this.tempList = new UnorderedListRefGeneric<>();
                    }
                } else {
                    if (arrayOrReferences.equals("a")) {
                        this.tempList = new ListArrayGeneric<>(1500000);
                    } else {
                        this.tempList = new LinkedListGeneric<>();
                    }
                }
                break;
            case "q":
                if (arrayOrReferences.equals("a")) {
                    this.tempQueue = new QueueArrayGeneric<>(1500000);
                } else {
                    this.tempQueue = new QueueRefGeneric<>();
                }
                break;
            case "s":
                if (arrayOrReferences.equals("a")) {
                    this.tempStack = new StackArrayGeneric<>(1500000);
                } else {
                    this.tempStack = new StackRefGeneric<>();
                }
                break;
            case "t":
                this.tempBST = new BSTRefGeneric<>();
                break;
            case "h":
                needHeapSort = true;
                this.tempHeap = new HeapArray<>(1500000);        
            case "a":
                this.tempAVL = new AVLTreeGeneric<>();
            default:
                break;
        }
        */
    }

    
    /*
    public BSTRefGeneric<Unihan> tempBST() {
        return this.tempBST;
    }
    public ListGeneric<Unihan> getTempList() {
        return this.tempList;
    }
    public QueueGeneric<Unihan> getTempQueue() {
        return this.tempQueue;
    }
    public StackGeneric<Unihan> getTempStack() {
        return this.tempStack;
    }
    public String getRegex () {
        return this.regex;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }
    */

    /**
     * This method loads a file into memory so it can be used
     */
    
    /*
    public void openFile(){
        this.text = new BufferedReader(new InputStreamReader(this.textToParse));
    }
    */

    /**
     * This method de-loads a file off memory
     */
    
    /*
    public void closeFile() {
        try {
            this.text.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    /**
     * This method reads a line: parse a complete line and skips over the next
     * @return read line - null if it is the end of the file
     */
   
    public String readLine() {
        String toReturn = "";
        try {
            toReturn = text.readLine();
        } catch (IOException e) {
            toReturn = "?";
        } catch (NoSuchElementException noSuchElementException) {
            toReturn = null;
        } finally {
            return toReturn;
        }
    }
    

    /**
     * This method takes the text loaded in memory, looks every line of it
     * Using regex, it finds the unicode characters and parse them so they can be added to the structures
     * And print the time it takes to store all characters it the structures so speed test can be performed
     * @return String with the time passed from the beginning to the end of the insertion
     */
    
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String addAll() {
        Instant firstTime = Instant.now();
        Pattern pattern = Pattern.compile(this.regex);
        String currentLine = readLine();
        while (currentLine!=null) {
            Matcher matcher = pattern.matcher(currentLine);
            if (matcher.find()) {
                String found = matcher.group();
                Unihan elementToAdd = new Unihan(found.substring(2));
                switch (this.dataStructure) {
                    case "h":
                        tempHeap.insertItem(elementToAdd);
                        break;
                    case "l":
                        tempList.insert(elementToAdd);
                        break;
                    case "q":
                        tempQueue.enqueue(elementToAdd);
                        break;
                    case "s":
                        tempStack.push(elementToAdd);
                        break;
                    case "t":
                        tempBST.insertBST(elementToAdd);
                        break;
                    default:
                        break;
                }
            }
            currentLine = readLine();
        }
        if (needSort) tempList.sort();
        if (needHeapSort) tempHeap.sort();
        Instant lastTime = Instant.now();
        String totalTime = Duration.between(firstTime, lastTime).toString();
        return "It took: "+ String.valueOf(totalTime);
    }
    */

    public Unihan parseChunck() {
        String chrStr = readLine();
        try {
            String[] englishDefinitions = stringToArray(readLine());
            String[] spanishDefinitions = stringToArray(readLine());
            String[] pictureLinks = stringToArray(readLine());
            String codePoint = readLine();
            int numOfStrokes = 0;
            String line = readLine();
            // Maneja el caso en el que el numOfStrokes tiene dos dígitos separados por espacio
            // Se toma solo el primero
            try {
                numOfStrokes = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                String[] s = line.split(" ");
                numOfStrokes = Integer.parseInt(s[0]);
            }
            String pinyin = readLine();
            String radix = readLine();
            String mp3file = readLine();
            Unihan thisChar = new Unihan(numOfStrokes,codePoint,mp3file,pinyin,radix,englishDefinitions,pictureLinks,spanishDefinitions);
            return thisChar;
        } catch (NoSuchElementException noSuchElementException) {
            Unihan emptyChar = new Unihan("U+0000");
            return emptyChar;
        } catch (NullPointerException nullPointerException) {
            Unihan emptyChar = new Unihan("U+0000");
            return emptyChar;
        }
    }
    
    int intRadix;
    
    // Funciona correctamente siempre que se lean los arcchivos individualmente (no el mergedFile)
    // No se deben leer las páginas 163 y 164 pues generan problemas
    public void parseText() {
        
        Unihan thisChar = parseChunck();
        
        this.strokesList[thisChar.getNumOfStrokes()] = thisChar.getNumOfStrokes();
        if (strokesList[thisChar.getNumOfStrokes()] == 0) {
            BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
            bstStrokesArray[thisChar.getNumOfStrokes()] = tempBST;
            bstStrokesArray[thisChar.getNumOfStrokes()].insertBST(thisChar);
        } else {
            if (bstStrokesArray[thisChar.getNumOfStrokes()] == null) {
                BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
                bstStrokesArray[thisChar.getNumOfStrokes()] = tempBST;
            }
            bstStrokesArray[thisChar.getNumOfStrokes()].insertBST(thisChar);
        }
        // Maneja los casos en los que hay comillas en el radical
        try {
            intRadix = (int) Math.floor(parseFloat(thisChar.getRadix()));
        } catch(NumberFormatException e) {
            String[] s = thisChar.getRadix().split("[.]", 0);
            if (s[0].charAt(s[0].length()-1) == '\'')
                s[0] = s[0].substring(0, s[0].length()-1);
            try {
                intRadix = Integer.parseInt(s[0]);
            }
            catch (NumberFormatException ex) {
                intRadix = 0;
            }
        }
        this.radixesList[intRadix] = intRadix;
        if (radixesList[intRadix] == 0) {
            tempBST = new BSTRefGeneric<>();
            bstRadixesArray[intRadix] = tempBST;
        } else {
            if (bstRadixesArray[intRadix] == null) {
                BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
                bstRadixesArray[intRadix] = tempBST;
            }
            bstRadixesArray[intRadix].insertBST(thisChar);
        } 
        tempAVL.setRoot(tempAVL.insert(tempAVL.getRoot(), thisChar));
        for (int i = 0; i < thisChar.getSpanishDefinitions().length; i++) {
            String def = thisChar.getSpanishDefinitions()[i];
            if (def.compareTo("NONE") == 0 || def.compareTo("NINGUNA") == 0) continue;
            SpanishDef sd = new SpanishDef(thisChar.getCharacter(), def);
            esDefArray.add(sd);
        }
        while (readLine()!= null) {
            Unihan currentChar = parseChunck();
            try {
                this.strokesList[currentChar.getNumOfStrokes()] = currentChar.getNumOfStrokes();
            } catch(ArrayIndexOutOfBoundsException e) {
                break;
            }    
            if (strokesList[currentChar.getNumOfStrokes()] == 0) {
                tempBST = new BSTRefGeneric<>();
                bstStrokesArray[currentChar.getNumOfStrokes()] = tempBST;
                bstStrokesArray[currentChar.getNumOfStrokes()].insertBST(currentChar);
            } else {
                if (bstStrokesArray[currentChar.getNumOfStrokes()] == null) {
                    BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
                    bstStrokesArray[currentChar.getNumOfStrokes()] = tempBST;
                }
                bstStrokesArray[currentChar.getNumOfStrokes()].insertBST(currentChar);
            }
            // Maneja los casos en los que hay comillas en el radical
            try {
                intRadix = (int) Math.floor(parseFloat(currentChar.getRadix()));
            } catch(NumberFormatException e) {
                String[] s = currentChar.getRadix().split("[.]", 0);
                if (s[0].charAt(s[0].length()-1) == '\'')
                    s[0] = s[0].substring(0, s[0].length()-1);
                intRadix = Integer.parseInt(s[0]);
            }
            this.radixesList[intRadix] = intRadix;
            if (radixesList[intRadix] == 0) {
                tempBST = new BSTRefGeneric<>();
                bstRadixesArray[intRadix] = tempBST;
            } else {
                if (bstRadixesArray[intRadix] == null) {
                    BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
                    bstRadixesArray[intRadix] = tempBST;
                }
                bstRadixesArray[intRadix].insertBST(currentChar);
            } 
            tempAVL.setRoot(tempAVL.insert(tempAVL.getRoot(), currentChar));
            for (int i = 0; i < currentChar.getSpanishDefinitions().length; i++) {
                String def = currentChar.getSpanishDefinitions()[i];
                if (def.compareTo("NONE") == 0 || def.compareTo("NINGUNA") == 0) continue;
                SpanishDef sd = new SpanishDef(currentChar.getCharacter(), def);
                esDefArray.add(sd);
            }
            
        }
    }
    
    // Búsqueda por caracter directamente
    // Nota: Se requeriría agregar una celda adicional a la app
    // La primera para buscar por caracter (searchByChar)
    // La segunda para buscar por patrón en español (searchPattern)
    public Unihan searchByChar(char c, char selector) {
        NodeGeneric<Unihan> n = null;
        if (selector == 'g') { // g de general
            n = this.tempAVL.find(tempAVL.getRoot(), c);  
        } else if (selector == 'f') { // f de favoritos
            n = this.favAVL.find(favAVL.getRoot(), c);  
        }
        return n.getData();
    }
    
    // Retorna una cola con los caracteres del árbol BST que corresponde a número de trazos s
    public QueueDynamicArrayGeneric<Unihan> filterByStrokes(int s, char selector) {
        if (selector == 'g')
            tempBST = this.bstStrokesArray[s];
        else if (selector == 'f')
            tempBST = this.favBSTStrokesArray[s];
        return tempBST.inOrderToQueue(tempBST.getRoot());
    }
    
    // Retorna una cola con los caracteres del árbol BST que corresponde al radical r
    public QueueDynamicArrayGeneric<Unihan> filterByRadixes(int r, char selector) {
        if (selector == 'g')
            tempBST = this.bstRadixesArray[r];
        else if (selector == 'f')
            tempBST = this.favBSTRadixesArray[r];
        return tempBST.inOrderToQueue(tempBST.getRoot());
    }
    
    // No se probó este método, creo que hay que hacerlo desde la misma app
    // Solo se debe verificar si si se agrega al AVL cuando se haga clic en agregar favorito
    // Todo lo demás debe funcionar correctamente porque es prácticamente igual a parseText
    // Y todo el parseText se verificó que funciona correctamente
    public void addFavorite(Unihan u) {
        favAVL.setRoot(favAVL.insert(favAVL.getRoot(), u));
        this.favStrokesList[u.getNumOfStrokes()] = u.getNumOfStrokes();
        if (favBSTStrokesArray[u.getNumOfStrokes()] == null) {
            BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
            favBSTStrokesArray[u.getNumOfStrokes()] = tempBST;
        } else {
            if (favBSTStrokesArray[u.getNumOfStrokes()] == null) {
                BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
                favBSTStrokesArray[u.getNumOfStrokes()] = tempBST;
            }
            favBSTStrokesArray[u.getNumOfStrokes()].insertBST(u);
        }
        intRadix = (int) Math.floor(parseFloat(u.getRadix()));
        this.favRadixesList[intRadix] = intRadix;
        if (favRadixesList[intRadix] == 0) {
            tempBST = new BSTRefGeneric<>();
            favBSTRadixesArray[intRadix] = tempBST;
        } else {
            if (favBSTRadixesArray[intRadix] == null) {
                BSTRefGeneric<Unihan> tempBST = new BSTRefGeneric<>();
                favBSTRadixesArray[intRadix] = tempBST;
            }
            bstRadixesArray[intRadix].insertBST(u);
        } 
    }
    
    // No se probó este método, creo que hay que hacerlo desde la misma app
    // Se debe verificar si al oprimir el botón eliminar favorito en la app, se elimina 
    // del arbol AVL de favoritos
    public void deleteFavorite(Unihan u) {
        favAVL.deleteNode(favAVL.getRoot(), u);
    }
    
    
    public MaxHeap searchPattern(String p) {
        MaxHeap heap = new MaxHeap();
        this.rabinKarp = new RabinKarp();
        for (int i = 0; i < esDefArray.size(); i++) {
            SpanishDef def = esDefArray.get(i);
            if (p.length() <= def.getLen()) {
                rabinKarp.search(def.getText(), p);
                // El RabinKarp se detiene la primera vez que haya encontrado el patrón
                // Y activa el flag Found que nos dice que el caracter debe agregarse al Heap
                if (rabinKarp.found) {
                    Unihan u = tempAVL.find(tempAVL.getRoot(), def.getChar()).getData();
                    u.setScore((double)p.length()/def.getText().length());
                    // Sentencia solo para verificar los puntajes obtenidos. Puede eliminarse 
                    System.out.println(u + " " + String.format("%.5f", u.getScore()));
                    heap.insert(u);
                }
            } else continue; // Si el patrón es mayor al texto en longitud no se ejecuta el rabinkarp
        }
        return heap;
    }
    
    // Método print solo para verificar si el orden en el AVL está bien
    // Puede modificarse según se necesite o eliminarse
    public void print() {
        System.out.println("Final Root: " + tempAVL.getRoot().getData());
        tempAVL.inOrder(tempAVL.getRoot());
    }
    
    
    
    /**
     * This method adds a character to a specific data structure
     * @param stringToAdd: String with the 4 or 5 symbols to be parsed to char
     * @return String with the time passed during he addition
     */
    
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String addElement(String stringToAdd) {
        Instant firstTime = Instant.now();
        Unihan elementToAdd = new Unihan(stringToAdd);
        switch(this.dataStructure) {
            case "h":
                this.tempHeap.insertItem(elementToAdd);
                break;
            case "l":
                this.tempList.insert(elementToAdd);
                break;
            case "q":
                this.tempQueue.enqueue(elementToAdd);
                break;
            case "s":
                this.tempStack.push(elementToAdd);
                break;
            case "t":
                this.tempBST.insertBST(elementToAdd);
            default:
                break;
        }
        Instant lastTime = Instant.now();
        String totalTime = Duration.between(firstTime, lastTime).toString();
        return "It took "+totalTime+" to add "+String.valueOf(elementToAdd);
    }
    */

    /**
     * This method removes all the elements from a specific data structure and displays in console
     * the time it took to do it.
     */
    
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String removeAll() {
        Instant firstTime = Instant.now();
        switch(this.dataStructure) {
            case "h":
                throw new UnsupportedOperationException("Not implemented yet");
            case "l":
                throw new UnsupportedOperationException("Not implemented yet");
            case "q":
                while (!tempQueue.empty()) {
                    this.tempQueue.dequeue();
                }
                break;
            case "s":
                while (!tempStack.empty()) {
                    this.tempStack.pop();
                }
                break;
            case "t":
                throw new UnsupportedOperationException("Not implemented yet");
            default:
                break;
        }
        Instant lastTime = Instant.now();
        String totalTime = Duration.between(firstTime, lastTime).toString();
        return "It took: "+ String.valueOf(totalTime);
    }
    */

    /**
     * This method removes a character from a specific data structure
     * When used with lists, a character is needed to be passed
     * @param stringToDelete String with four or five hexadecimal symbols of the han character.
     */
    
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String removeElement(String stringToDelete) {
        boolean removed = false;
        Unihan toDelete = new Unihan(stringToDelete);
        Instant firstTime = Instant.now();
        String message;
        switch (dataStructure) {
            case "h":
                throw new UnsupportedOperationException("Not implemented yet");
            case "l":
                try {
                    removed = this.tempList.delete(toDelete);
                } catch (NullPointerException nullPointerException) {
                    return nullPointerException.getMessage();
                }
                break;
            case "q":
                try {
                    toDelete = this.tempQueue.dequeue();
                    removed = true;
                } catch (NullPointerException nullPointerException) {
                    return nullPointerException.getMessage();
                }
                break;
            case "s":
                try {
                    toDelete = this.tempStack.pop();
                    removed = true;
                } catch (NullPointerException nullPointerException) {
                    return nullPointerException.getMessage();
                }
                break;
            case "t":
                throw new UnsupportedOperationException ("Not implemented yet");
            default:
                break;
        }
        Instant lastTime = Instant.now();
        String totalTime = Duration.between(firstTime, lastTime).toString();
        if (removed) {
            return "It took "+totalTime+ " seconds to delete "+String.valueOf(toDelete);
        } else {
            return String.valueOf(toDelete)+" was not removed.";
        }
    }
    */

    /**
     * This method shows the length of the data struture.
     */
    
    /*
    public String getDataStructureLength() {
        int size;
        switch(this.dataStructure) {
            case "l":
                size = tempList.length();
                break;
            case "q":
                size = tempQueue.length();
                break;
            case "s":
                size = tempStack.length();
                break;
            default:
                size = -1;
                break;
        }
        return String.valueOf(size);
    }
    */

    /**
     * This method prints the characters of the data structures
     */
    
    /*
    public String print() {
        String toReturn;
        switch(this.dataStructure) {
            case "l":
                toReturn = tempList.toString();
                break;
            case "q":
                toReturn = tempQueue.toString();
                break;
            case "s":
                toReturn = tempStack.toString();
                break;
            default:
                toReturn = "";
                break;
        }
        return toReturn;
    }
    */

    // Modificado para que las definiciones se dividan por ', ' y no solo por comas
    // Agregados casos en los que hay comillas para eliminarlas adecuadamente
    private String[] stringToArray(String strToParse) {
        strToParse = strToParse.substring(1,strToParse.length()-1);
        String[] data = strToParse.split("', '");
        for (int i=0; i<data.length; i++) {
            try {
                if (data[i].charAt(0) == '\'' && data[i].charAt(data[i].length()-1) == '\'')
                    data[i] = data[i].substring(1,data[i].length()-1);
                else if (data[i].charAt(0) == '\'' && data[i].charAt(data[i].length()-1) != '\'')
                    data[i] = data[i].substring(1,data[i].length());
                else if (data[i].charAt(0) != '\'' && data[i].charAt(data[i].length()-1) == '\'')
                    data[i] = data[i].substring(0,data[i].length()-1);
                else
                    data[i] = data[i].substring(0,data[i].length());
            } catch(StringIndexOutOfBoundsException e) {
                
            }
        }
        return data;
    }
}