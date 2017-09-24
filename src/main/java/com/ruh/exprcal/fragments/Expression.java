/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.abstractions.Symbol;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A fragment of 'Expression' type
 */
public class Expression extends ExpressionFragment {
    /**
     * Stack to hold the symbol list of the expression during execution
     */
    private Stack<Symbol> symbols = new Stack<>();
    /**
     * Stack for holding the numbers,constants,functions and expressions
     */
    private Stack<ExpressionFragment> numbers = new Stack<>();
    /**
     * Holds the whole fragment map
     */
    private TreeMap<Integer, ExpressionFragment> frag = new TreeMap<Integer, ExpressionFragment>();
    /**
     * Holds the distributed fragment map
     */
    private Multimap<String, ExpressionFragment> fragDistMap = ArrayListMultimap.create();
    /**
     * Holds the final result after execution
     */
    private double result;
    /**
     * The trigonometric flag
     */
    private static int trigFlag;
    /**
     * The round up scale
     */
    private static int roundScale;
    /**
     * The solution available flag for caching
     */
    private boolean solved=false;

    /**
     * The constructor that makes a Expression fragment.
     * @param pos the position where the expression will exist
     * @param s the expression in String format
     * @param trigFl the DEGREE flag (set:- <code>ExpressionRenderer.DEGREE</code> or <code>ExpressionRenderer.RADIAN</code>)
     * @param roundSc the rounding up scale (set:- Any integer no. within 0-9)
     * @throws BadExpressionFragmentException if expression fragments are unsupported
     * @throws BadExpressionException if expression can't be parsed
     */
    public Expression(int pos, String s, int trigFl, int roundSc) throws BadExpressionFragmentException, BadExpressionException {
        super(pos, s);
        trigFlag = trigFl;
        roundScale = roundSc;
        formParseMap(trimSpaces(s));
        sample();
    }
    
    /**
     * Trim the spaces in the {@code String} expression
     * @param s the expression in {@code String} format
     * @return the re-formated {@code String} expression
     */
    private String trimSpaces(String s)
    {
        String temp="";
        for (int i=0;i<s.length();i++)
        {
            if (s.charAt(i)!=' ')
                temp += s.charAt(i);
        }
        return temp;
    }

    /**
     * Return a list of all the Fragments of {@code type}
     * @param type the type of fragments 
     * <ul>
     * <li>ExpressionFragment.FRAG_NUM = "NUMBER"</li>
     * <li>ExpressionFragment.FRAG_OPT = "OPERATOR"</li>
     * <li>ExpressionFragment.FRAG_SIGN = "SIGN"</li>
     * <li>ExpressionFragment.FRAG_BRACK = "BRACKET"</li>
     * <li>ExpressionFragment.FRAG_EXPR = "EXPRESSION"</li>
     * <li>ExpressionFragment.FRAG_FUNC = "FUNCTION"</li>
     * <li>ExpressionFragment.FRAG_CONS = "CONSTANT"</li>
     * </ul>
     * @return the list containing the fragments
     */
    public List<ExpressionFragment> get(String type) {
        List<ExpressionFragment> temp = new ArrayList<>(fragDistMap.get(type));
        return temp;
    }

    /**
     * Returns a reference to the fragment map
     * @return the reference to the fragment map
     */
    public SortedMap<Integer, ExpressionFragment> getFragmentMap() {
        return frag;
    }

    /**
     * Returns the result of the expression in com.ruh.exprcal.fragments.Number format
     * @return the result in com.ruh.exprcal.fragments.Number format
     * @throws BadExpressionFragmentException if number format is not supported
     */
    public Number getResult() throws BadExpressionFragmentException {
        return new Number(BASIC_POS, result);
    }

    /**
     * Forms the parse map for the expression
     * @param s the expression in {@code String} format
     * @throws BadExpressionFragmentException if paradigm is violated
     * @throws BadExpressionException if expression isn't supported
     */
    private void formParseMap(String s) throws BadExpressionFragmentException, BadExpressionException {
        s = '(' + s + ')';
        String temp = "";
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isUpperCase(ch))
            {
                if (!"".equals(temp)) {

                    for (String e : extractNumberBlocks(temp)) {
                        int id = (frag.isEmpty()) ? 0 : (frag.size());
                        frag.put(id, new Number(id, e));
                        fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));
//                        System.out.println(id + " Num " + frag.get(id));
                    }

                    temp = "";
                }
                String concat = ""; int k=0;
                for (k=i;k<s.length();k++)
                {
                    if (Character.isUpperCase(s.charAt(k)))
                        concat += s.charAt(k);
                    else
                        break;
                }
                int id = (frag.isEmpty()) ? 0 : (frag.size());

                    frag.put(id, (ExpressionFragment) new Constant(id,concat,trigFlag));
                    fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));
                    i=k-1;
//                    System.out.println(id + " Constant " + frag.get(id));
            }
            else if (Character.isLowerCase(ch)) {
                if (!"".equals(temp)) {

                    for (String e : extractNumberBlocks(temp)) {
                        int id = (frag.isEmpty()) ? 0 : (frag.size());
                        frag.put(id, new Number(id, e));
                        fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));
//                        System.out.println(id + " Num " + frag.get(id));
                    }

                    temp = "";
                }
                int j;
                String concat = "";
                int param = 0, flag = 1;
                for (j = i; j < s.length(); j++) {

                    if (Character.isAlphabetic(s.charAt(j)) && flag == 1) {
                        concat += s.charAt(j);
                    } else if (s.charAt(j) == '(') {
                        flag = 0;
                        param++;
                        if (!Function.exists(concat)) {
                            throw new BadExpressionFragmentException("Not a Function", concat);
                        }
                        if (param == 0) {
                            break;
                        }
                    } else if (s.charAt(j) == ')') {
                        flag = 0;
                        param--;
                        if (!Function.exists(concat)) {
                            throw new BadExpressionFragmentException("Not a Function", concat);
                        }
                        if (param == 0) {
                            break;
                        }
                    }
                }
                if (param == 0) {
                    int id = (frag.isEmpty()) ? 0 : (frag.size());

                    frag.put(id, (ExpressionFragment) new Function(id, s.substring(i, j + 1), trigFlag, roundScale)/*FUNC_TYPE.cast(new ExtendedFunction(id,s.substring(i,j+1)))*/);
                    fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));

//                    System.out.println(id + " Function " + frag.get(id));
                    i = j;
                } else {
                    throw new BadExpressionException("Not an Expression", s);
                }
            } else if (Character.isDigit(ch) || ch == '.') {
                temp += ch;
            } else {

                if (!"".equals(temp)) {

                    for (String e : extractNumberBlocks(temp)) {
                        int id = (frag.isEmpty()) ? 0 : (frag.size());
                        frag.put(id, new Number(id, e));
                        fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));
//                        System.out.println(id + " Num " + frag.get(id));
                    }

                    temp = "";
                }

                int id = (frag.isEmpty()) ? 0 : (frag.size());

                if (Operator.exists(ch)) {
                    int type = Operator.returnOpt((ExpressionFragment) frag.lastEntry().getValue(), ch, s.charAt(i + 1));
                    switch (type) {
                        case Operator.BINARY:
                            frag.put(id, new Operator(id, ch));
                            fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));
//                            System.out.println(id + " Opt " + frag.get(id));
                            break;
                        case Operator.DUAL:
                            frag.put(id, new Sign(id, ch));
                            fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));
//                            System.out.println(id + " Sign " + frag.get(id));
                            break;
                    }
                } else if (Bracket.exists(ch)) {
                    id = (frag.isEmpty()) ? 0 : (frag.size());
                    frag.put(id, new Bracket(id, ch));
                    fragDistMap.put(frag.get(id).getFragmentType(), frag.get(id));
//                    System.out.println(id + " Bracket " + frag.get(id));
                } else {

                    throw new BadExpressionException("Not an expresison", frag.toString());
                }

            }

        }
    }

    /**
     * Form a distributed fragment map
     * @return the reference to self
     */
    private Expression formFragDistributedMap() {
        fragDistMap.clear();
        for (Map.Entry<Integer, ExpressionFragment> node : frag.entrySet()) {
            ExpressionFragment elem = node.getValue();
            fragDistMap.put(elem.getFragmentType(), elem);
        }
        return this;
    }

    /**
     * Form a fragment parse map
     * @param map a fragment map
     * @return the reference to self
     */
    private Expression formParseMap(SortedMap<Integer, ExpressionFragment> map) {
        this.frag = (TreeMap<Integer, ExpressionFragment>) map;
        return this;
    }

    /**
     * Samples the expression fragment map to follow a standard form
     * @throws BadExpressionException if the expression doesn't follow standard expression paradigms
     * @throws BadExpressionFragmentException if the fragments doesn't follow standard paradigms
     */
    private void sample() throws BadExpressionException, BadExpressionFragmentException {
        if (!isParanthasized()) {
            throw new BadExpressionException("Not Paranthasized", getValue());
        }

        int inc = 0;

        for (SignBlock elem : extractSignBlocks()) {
            List<SortedMap<Integer, ExpressionFragment>> parts = splitExp(elem.start + inc, elem.end + inc);
            Number ob = Sign.multiply(parts.get(1));
            TreeMap<Integer, ExpressionFragment> map_temp = new TreeMap<>();

            if (!frag.get(elem.start + inc - 1).isOpt() && (frag.get(elem.start + inc - 1).isBracket() && ((Bracket) frag.get(elem.start + inc - 1)).isClose())) {
                map_temp.put(BASIC_POS, new Operator(BASIC_POS, '*'));
                inc++;
            }
            map_temp.put((map_temp.isEmpty()) ? BASIC_POS : map_temp.size(), ob.setPos((map_temp.isEmpty()) ? BASIC_POS : map_temp.size()));
            map_temp.put(map_temp.size(), new Operator(map_temp.size(), '*'));
            inc += 2;
            parts.set(1, map_temp);
            inc -= (elem.end - elem.start + 1);

            formParseMap(mergeExp(parts));

        }

        SortedMap<Integer, ExpressionFragment> map_temp = new TreeMap<>();
        Queue<Integer> q_temp = extractMultiplicationPoints();

        for (int j = 0; j <= frag.lastKey(); j++) {
            int id = (map_temp.isEmpty()) ? 0 : (map_temp.size());
            map_temp.put(id, (frag.get(j)).setPos(id));
            if (!q_temp.isEmpty() && q_temp.peek() == j) {
                map_temp.put(map_temp.size(), new Operator(id, '*'));
                q_temp.remove();
            }
        }
        formParseMap(map_temp);
        formFragDistributedMap();
    }

    /**
     * Solves the expression and returns the reference to self
     * @return the the reference to self
     * @throws BadExpressionException if the expression doesn't follow standard expression paradigms
     * @throws BadExpressionFragmentException if the fragments doesn't follow standard paradigms
     */
    public Expression solve() throws BadExpressionFragmentException, BadExpressionException {
        if (solved)
            return this;
        
        SortedMap<Integer, ExpressionFragment> frag_temp = frag;
        int size = frag_temp.firstKey() + frag_temp.size();
        for (int i = frag_temp.firstKey(); i < size; i++) {
            ExpressionFragment elem = frag_temp.get(i);

            if (elem.isBracket() && ((Bracket) elem).isOpen()) {
                symbols.push((Bracket) elem);
            } else if (elem.isOpt() || elem.isBracket()) {

                while ( (((Symbol) elem).getPriority() <= ((Symbol) symbols.peek()).getPriority()) && !((ExpressionFragment)symbols.peek()).isBracket() ) {
                    Number a = (Number) numbers.pop();
                    Number b = (Number) numbers.pop();
                    numbers.push(Operator.operate(b, (Operator) symbols.pop(), a));
                }

                if (elem.isBracket() && ((Bracket) elem).isClose()) {
                    symbols.pop();
                } else {
                    symbols.push((Operator) elem);
                }

            } else {
                if (elem.isFunction()) {
                    numbers.push(((Function) elem).process().getResult());
                }
                else if (elem.isConstant()) {
                    numbers.push(((Constant) elem).get());
                }
                else {
                    numbers.push((Number) elem);
                }
            }
        }
        result = new BigDecimal(((Number) (numbers.pop())).getNumber()).setScale(roundScale, RoundingMode.CEILING).doubleValue();
        solved=true;
        return this;
    }

    /**<pre>
     * Extract Number blocks from a complex number string 
     * viz:- 1.2223.679.4713.3 is evaluated as 1.2223 * .679 * .4713 * .3</pre>
     * @param s the number block in string format
     * @return the list that has the evaluated numbers
     */
    private List<String> extractNumberBlocks(String s) {
        List<String> ob = new ArrayList<>();
        String temp[] = s.split("[.]");
        if (temp.length == 1) {
            ob.add(s);
        } else {
            for (int k = 1; k < temp.length; k++) {
                if (k == 1) {
                    ob.add(temp[k - 1] + "." + temp[k]);
                } else {
                    ob.add("." + temp[k]);
                }

            }
        }
        return ob;
    }

    /**
     * Extract Multiplication points from the fragment map
     * @return a queue of every multiplication point in the map
     */
    private Queue<Integer> extractMultiplicationPoints() {
        Queue<Integer> temp = new LinkedList<>();
        for (int i = 0; i < frag.lastKey() - 1; i++) {
            ExpressionFragment elem = frag.get(i);
            ExpressionFragment post = frag.get(i + 1);
            if (elem.isNumber() || elem.isFunction() || elem.isConstant() || (elem.isBracket() && ((Bracket) elem).isClose())) {
                if (post.isNumber() || post.isFunction() || post.isConstant() || (post.isBracket() && ((Bracket) post).isOpen())) {
                    temp.add(i);
                }
            }
        }

        return temp;
    }

    /**
     * Extract Sign blocks from the fragment map
     * @return a list of Sign blocks
     */
    private List<SignBlock> extractSignBlocks() {
        List<SignBlock> ob = new ArrayList<>();
        int pre_hold;
        for (ExpressionFragment elem : this.fragDistMap.get(ExpressionFragment.FRAG_SIGN)) {
            pre_hold = elem.getPos();
            if (!ob.isEmpty()) {
                if (elem.getPos() <= ob.get(ob.size() - 1).end) {
                    continue;
                }
            }
            for (int i = pre_hold; i < frag.size(); i++) {
                if (!frag.get(i).isSign()) {
                    ob.add(new SignBlock(pre_hold, i - 1));
                    break;
                }
            }
        }
        return ob;
    }

    /**
     * Returns a list of two fragment maps split from the original
     * @param p the point from where it's split
     * @return a list of two fragment maps split from the original
     */
    private List<SortedMap<Integer, ExpressionFragment>> splitExp(int p) {
        List<SortedMap<Integer, ExpressionFragment>> parts = new ArrayList<>();
        parts.add(frag.subMap(0, p));
        parts.add(frag.subMap(p, frag.size()));
        return parts;
    }

    /**
     * Returns a list of three fragment maps split from the original
     * @param p the point from where it's split
     * @param q the second point from where it's split
     * @return a list of three fragment maps split from the original
     */
    private List<SortedMap<Integer, ExpressionFragment>> splitExp(int p, int q) {
        List<SortedMap<Integer, ExpressionFragment>> parts = new ArrayList<>();
        parts.add(frag.subMap(0, p));
        parts.add(frag.subMap(p, q + 1));
        parts.add(frag.subMap(q + 1, frag.size()));
        return parts;
    }

    /**
     * Returns a Map of merged smaller maps
     * @param parts the list of smaller maps
     * @return the map of merged smaller maps
     */
    private SortedMap<Integer, ExpressionFragment> mergeExp(List<SortedMap<Integer, ExpressionFragment>> parts) {
        TreeMap<Integer, ExpressionFragment> map = new TreeMap<>();
        int j = 0;
        fragDistMap.clear();
        for (int i = 0; i < parts.size(); i++) {
            for (Map.Entry<Integer, ExpressionFragment> elem : parts.get(i).entrySet()) {
                map.put(j++, elem.getValue().setPos(j));
            }
        }
        return map;
    }

    /**
     * Checks whether the expression is well balanced
     * @return {@code true} if expression is well closed, otherwise {@code false}
     */
    public boolean isParanthasized() {
        int param = 0;
        for (ExpressionFragment elem : this.fragDistMap.get(ExpressionFragment.FRAG_BRACK)) {
            if (((Bracket) elem).isOpen()) {
                param++;
            } else {
                param--;
            }
        }
        return (param == 0);
    }

    @Override
    public String toString() {

        String s_temp = "";

        for (Map.Entry<Integer, ExpressionFragment> elem : this.frag.entrySet()) {
            s_temp += elem.getValue();
        }

        return s_temp;
    }

    @Override
    public boolean isOpt() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isBracket() {
        return false;
    }

    @Override
    public boolean isSign() {
        return false;
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public boolean isFunction() {
        return false;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    /**
     * Holds the left and right parameters of a sign block
     */
    private class SignBlock implements java.io.Serializable {
        /**
         * <ul>
         * <li>start - left of the sign block</li>
         * <li>end - right of the sign block</li>
         * </ul>
         */
        int start, end;

        public SignBlock(int p, int q) {
            this.start = p;
            this.end = q;
        }

        @Override
        public String toString() {
            return "{start:- " + start + ", end:- " + end + "}";
        }

    }

}
