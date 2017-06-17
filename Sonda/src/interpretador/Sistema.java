package interpretador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Gustavo
 */
public class Sistema {

    private Long id;
    private List<String> tokens;
    private Integer x;
    private Integer y;
    private Boolean change;
    private List<String> listOfTokens;
    private List<Integer> listOfValues;
    private String lastToken;

    public Sistema() {
        this.id = 1L;
        this.tokens = new ArrayList<>();
        this.x = 0;
        this.y = 0;
        this.change = false;
        this.listOfTokens = new ArrayList<>();
        this.listOfValues = new ArrayList<>();
        this.lastToken = "";
    }

    public void realizaInterpretacao() throws FileNotFoundException, IOException {
        BufferedReader in;
        FileReader fe;
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { //selecao da arquivo
            fe = new FileReader(jfc.getSelectedFile());
            in = new BufferedReader(fe);
        } else {
            return;
        }

        String linha;
        loop1:
        while (null != (linha = in.readLine())) {
            String[] array = linha.split(" ");
            this.tokens.clear();
            this.listOfTokens.clear();
            this.listOfValues.clear();
            for (int i = 0; i < array.length; i++) {
                if (linha.contains("(")) {
                    realizaTratamentoParenteses(array, linha, i);
                    continue loop1;
                } else {
                    calculaInfo(array, i, true);
                }
            }
            alteraPos();
            printStatistics();
        }
        System.out.println(this.x + "." + this.y);
    }

    private void realizaTratamentoParenteses(String[] array, String linha, Integer i) {
        if (i == 0) {
            for (int j = 0; j < array.length; j++) {
                comando(array[j]);
            }
            System.out.println("-------------------------------------------------");
        }
        this.change = Boolean.FALSE;
        linha = linha.replace(")", ";");
        linha = linha.replace("(", ";");
        String[] arrayAux = linha.split(";");
        arrayAux = alteraPos(arrayAux);
        loop2:
        for (int j = 0; j < arrayAux.length; j++) {
            String[] arrayAux2 = arrayAux[j].split(" ");
            for (int k = 0; k < arrayAux2.length; k++) {
                if (arrayAux2[k].trim().equalsIgnoreCase("")
                        || arrayAux2[k].trim().equalsIgnoreCase("APOS")) {
                    continue;
                }
                calculaInfo(arrayAux2, k, false);
            }
            alteraPos();
            printStatistics();
            this.listOfTokens.clear();
            this.listOfValues.clear();
        }
    }

    private void alteraPos() {
        for (int i = 0; i < this.tokens.size(); i++) {
            if (this.tokens.get(i).equalsIgnoreCase("APOS")) {
                String aux = this.tokens.get(i - 1);
                this.tokens.set(i - 1, this.tokens.get(i + 1));
                this.tokens.set(i + 1, aux);
            }
        }
    }

    private String[] alteraPos(String[] array) {
        for (int i = 0; i < array.length; i++) {
            try {
                if (array[i].trim().equalsIgnoreCase("APOS")) {
                    String aux = array[i - 1];
                    array[i - 1] = array[i + 1];
                    array[i + 1] = aux;
                } else if (array[i].substring(0, 4).trim().equalsIgnoreCase("APOS")
                        || array[i].substring(array[i].length() - 5, array[i].length()).trim().equalsIgnoreCase("APOS")) {
                    String aux = array[i].replace("APOS", "");
                    array[i] = array[i + 1];
                    array[i + 1] = aux;
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return array;
    }

    private void printStatistics() {
        for (int i = 0; i < this.listOfValues.size(); i++) {
            if (this.listOfTokens.get(i).trim().equalsIgnoreCase("FRENTE")) {
                System.out.println("Movimentou Frente " + this.listOfValues.get(i));
                this.x += this.listOfValues.get(i);
            } else if (this.listOfTokens.get(i).trim().equalsIgnoreCase("TRAS")) {
                System.out.println("Movimentou Trás " + this.listOfValues.get(i));
                this.x -= listOfValues.get(i);
            } else if (listOfTokens.get(i).trim().equalsIgnoreCase("ESQUERDA")) {
                System.out.println("Movimentou Esquerda " + this.listOfValues.get(i));
                this.y -= this.listOfValues.get(i);
            } else if (listOfTokens.get(i).trim().equalsIgnoreCase("DIREITA")) {
                System.out.println("Movimentou Direita " + this.listOfValues.get(i));
                this.y += this.listOfValues.get(i);
            }
        }
        if (!this.listOfValues.isEmpty()) {
            System.out.println("-------------------------------------------------");
        }
    }

    private void calculaInfo(String[] array, Integer i, Boolean showComando) {
        try {
            this.listOfValues.add(Integer.parseInt(array[i].trim()));
            this.listOfTokens.add(this.lastToken);
            if (this.change) {
                this.change = Boolean.FALSE;
                String aux = this.listOfTokens.get(this.listOfTokens.size() - 1);
                Integer auxInt = listOfValues.get(this.listOfTokens.size() - 1);
                String aux2 = this.listOfTokens.get(this.listOfTokens.size() - 2);
                Integer auxInt2 = this.listOfValues.get(this.listOfTokens.size() - 2);
                for (int j = 0; j < 2; j++) {
                    this.listOfValues.remove(this.listOfValues.size() - 1);
                    this.listOfTokens.remove(this.listOfTokens.size() - 1);
                }
                this.listOfTokens.add(aux);
                this.listOfValues.add(auxInt);
                this.listOfTokens.add(aux2);
                this.listOfValues.add(auxInt2);
                /*mudar ordem*/
            }
        } catch (NumberFormatException e) {
            if (showComando) {
                comando(array[i].trim());
            }
            this.lastToken = array[i].trim();
        }
    }

    private void comando(String token) {
        if (token.contains("(")) {
            System.out.println("comando -> (comando)");
            token = token.replace("(", "");
            token = token.replace(")", "");
        } else if (token.contains(")")) {
        }
        switch (token) {
            case "ENTAO":
                this.tokens.add("ENTAO");
                System.out.println("comando -> comando ENTAO comando");
                break;
            case "APOS":
                this.tokens.add("APOS");
                this.change = true;
                System.out.println("comando -> comando APOS comando");
                break;
            default:
                basico(token);
                break;
        }
    }

    private void basico(String token) {
        switch (token) {
            case "FRENTE":
                this.tokens.add("FRENTE");
                System.out.println("comando -> basico -> FRENTE n");
                break;
            case "ESQUERDA":
                this.tokens.add("ESQUERDA");
                System.out.println("comando -> basico -> ESQUERDA n");
                break;
            case "DIREITA":
                this.tokens.add("DIREITA");
                System.out.println("comando -> basico -> DIREITA n");
                break;
            case "TRAS":
                this.tokens.add("TRAS");
                System.out.println("comando -> basico -> TRAS n");
                break;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Boolean getChange() {
        return change;
    }

    public void setChange(Boolean change) {
        this.change = change;
    }

    public List<String> getListOfTokens() {
        return listOfTokens;
    }

    public void setListOfTokens(List<String> listOfTokens) {
        this.listOfTokens = listOfTokens;
    }

    public List<Integer> getListOfValues() {
        return listOfValues;
    }

    public void setListOfValues(List<Integer> listOfValues) {
        this.listOfValues = listOfValues;
    }

    public String getLastToken() {
        return lastToken;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }
}
