import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        HashMap<String, ArrayList<String>> fd = new HashMap<>();

//        // Example 1
//        fd.put("A",new ArrayList<String>());
//        fd.put("BC",new ArrayList<String>());
//        fd.put("C",new ArrayList<String>());
//        fd.put("E",new ArrayList<String>());
//        fd.get("A").add("D");
//        fd.get("BC").add("AD");
//        fd.get("C").add("B");
//        fd.get("E").add("AD");

        // Example 2
//        fd.put("AC",new ArrayList<String>());
//        fd.put("BC",new ArrayList<String>());
//        fd.put("D",new ArrayList<String>());
//        fd.put("CG",new ArrayList<String>());
//        fd.put("ACD",new ArrayList<String>());
//        fd.put("CE",new ArrayList<String>());
//        fd.get("AC").add("G");
//        fd.get("BC").add("D");
//        fd.get("CG").add("BD");
//        fd.get("D").add("EG");
//        fd.get("ACD").add("B");
//        fd.get("CE").add("AG");

        // Example 3
//        fd.put("A",new ArrayList<String>());
//        fd.put("B",new ArrayList<String>());
//        fd.put("AB",new ArrayList<String>());
//        fd.get("A").add("BC");
//        fd.get("B").add("C");
//        fd.get("AB").add("C");

        // Example 4
//        fd.put("B",new ArrayList<String>());
//        fd.put("AD",new ArrayList<String>());
//        fd.put("C",new ArrayList<String>());
//        fd.get("B").add("A");
//        fd.get("AD").add("BC");
//        fd.get("C").add("ABD");

        // Example 5
//        fd.put("XYZ",new ArrayList<String>());
//        fd.put("XY",new ArrayList<String>());
//        fd.put("X",new ArrayList<String>());
//        fd.get("XYZ").add("W");
//        fd.get("XY").add("ZW");
//        fd.get("X").add("YZW");

        // Example 6
//        fd.put("XY",new ArrayList<String>());
//        fd.put("Z",new ArrayList<String>());
//        fd.put("W",new ArrayList<String>());
//        fd.get("XY").add("Z");
//        fd.get("Z").add("YW");
//        fd.get("W").add("X");

        //Example 7
        fd.put("CH",new ArrayList<String>());
        fd.put("E",new ArrayList<String>());
        fd.put("A",new ArrayList<String>());
        fd.put("F",new ArrayList<String>());
        fd.put("B",new ArrayList<String>());
        fd.get("CH").add("G");
        fd.get("E").add("A");
        fd.get("A").add("BC");
        fd.get("F").add("EG");
        fd.get("B").add("CFH");

//        System.out.print("Enter number of FDs : ");
//        int f = in.nextInt();
//        in.nextLine();
//
//        for (int i = 0; i < f; i++) {
//            System.out.println("Enter LHS :");
//            String l = in.nextLine();
//            System.out.println("Enter RHS :");
//            String r = in.nextLine();
//            ArrayList<String> al = new ArrayList<>();
//            al.add(r);
//            if(fd.containsKey(l)){
//                ArrayList<String> temp = fd.get(l);
//                al.addAll(temp);
//            }
//            fd.put(l, al);
//        }

        System.out.print("Enter Attributes in Relation: ");
        String r = in.nextLine();

        System.out.println("\nGiven FDs :");
        printMap(fd);

        System.out.println("\n\n***Finding Candidate Keys***\n");
        getCandidateKeys(fd, r);

        System.out.println("\n\n***Finding Canonical Cover***\n");
        getCanonicalCover(fd);

        System.out.println("\n\n*** THANK YOU ***\n\n");
    }

    public static void printMap(HashMap<String, ArrayList<String>> hm){
        for(String s : hm.keySet()){
            System.out.println(s + " -> " + hm.get(s));
        }
    }

    public static void getCandidateKeys(HashMap<String, ArrayList<String>> fd, String rel){
        // Finding Candidate Keys...

        HashSet<String> L = new HashSet<>(), R = new HashSet<>(), M = new HashSet<>(), UNION =
                new HashSet<>();
        for(String s : fd.keySet()){
            char[] l = s.toCharArray();
            for(char c : l){
                L.add(String.valueOf(c));
                UNION.add(String.valueOf(c));
            }
            for(String r : fd.get(s)){
                char[] temp = r.toCharArray();
                for(char c : temp) {
                    R.add(String.valueOf(c));
                    UNION.add(String.valueOf(c));
                }
            }
        }

        ArrayList<String> toRemove = new ArrayList<>();
        for(String s : L){
            if(R.contains(s)){
                M.add(s);
                toRemove.add(s);
            }
        }

        for(String s : toRemove){
            L.remove(s);
            R.remove(s);
        }

        ArrayList<String> ck = new ArrayList<>();
        String must = "", mids = "", all = rel;

        char[] temp = rel.toCharArray();
        for(char c : temp){
            if(!UNION.contains(String.valueOf(c))){
                must += c;
            }
        }
//        System.out.println("MUST = " + must);
        for(String s : L) must += s;
        for(String s : M) mids += s;
        all = sortString(all);
        must = sortString(must);

        ArrayList<String> combinations = printPowerSet(mids.toCharArray(), mids.length());
        for(String s : combinations){
            String cur = must + s;
            cur = sortString(cur);

            String closure = getClosure(fd, cur, cur);
//            System.out.println("Current = " + cur);
//            System.out.println("Closure = " + closure);

            if(closure.equals(all)){
                int add = 1;
                for(String cks : ck){
                    HashSet<Character> a = new HashSet<>(), b = new HashSet<>(), remove = new HashSet<>();
                    temp = cks.toCharArray();
                    for(char c : temp) b.add(c);
                    temp = cur.toCharArray();
                    for(char c : temp){
                        b.remove(c);
                    }

                    if(b.isEmpty()){
                        add = 0;
                        break;
                    }


//                    if(cur.contains(cks)){
//                        add = 0;
//                        break;
//                    }
                }
                if(add == 1) ck.add(cur);
            }
        }

        System.out.println("*Candidate Keys :- ");
        for(String s : ck){
            System.out.println(s);
        }
    }

    public static void getCanonicalCover(HashMap<String, ArrayList<String>> fd){
        // Finding Canonical Cover....

        // Step 1 : Make Singleton RHS

        for(String s : fd.keySet()){
            String r = fd.get(s).get(0);
            if(r.length()>1){
                char[] c = r.toCharArray();
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < c.length; i++) {
                    temp.add(String.valueOf(c[i]));
                }
                fd.put(s, temp);
            }
        }

        System.out.println("Step 1 : Make Singleton RHS");
        System.out.println("---------ReWriting FDs----------");
        printMap(fd);
        System.out.println("\nStep 1 Over!\n\n");

        // Step 2 : No extraneous attribute

        boolean karteHai = true;
        while(karteHai) {

            karteHai = false;
            String toRemove = "";

            for (String s : fd.keySet()) {
                ArrayList<String> rhs = fd.get(s);
                if (s.length() > 1) {
                    ArrayList<String> combinations = printPowerSet(s.toCharArray(), s.length());
                    combinations.remove(0);
                    combinations.remove(s);
                    //pass map of fd, current string and current combination to getClosure
                    for (String curComb : combinations) {

                        String closure = getClosure( fd,s, curComb);
                        HashSet<Character> total = new HashSet<>(), toSubtract = new HashSet<>();
                        char[] temp = s.toCharArray();
                        for (Character c : temp) {
                            total.add(c);
                        }
                        temp = curComb.toCharArray();
                        for (Character c : temp) {
                            toSubtract.add(c);
                        }
                        HashSet<Character> diff = new HashSet<>();
                        for (Character c : total) {
                            if (!toSubtract.contains(c)) diff.add(c);
                        }

                        String toCheck = "";
                        for (Character c : diff) toCheck += c;

                        if (closure.contains(toCheck)) {
                            karteHai = true;
                            String updated = "";
                            for (char c : s.toCharArray()) {
                                if (toCheck.contains(Character.toString(c))) continue;
                                updated += c;
                            }
                            System.out.println("**FD Update : " + s + " --> " + updated);
                            if(fd.containsKey(updated)){
                                ArrayList<String> al = fd.get(updated);
                                for(String st : al){
                                    if(!rhs.contains(st)) rhs.add(st);
                                }
                            }
                            fd.put(updated, rhs);
                            toRemove = s;
                            break;
                        }
                    }
                }
            }
            if(karteHai){
                fd.remove(toRemove);
            }
        }

        System.out.println("Step 2 : Remove extraneous attribute");
        System.out.println("---------ReWriting FDs----------");
        printMap(fd);
        System.out.println("\nStep 2 over!!\n\n");

        // Step 3 : Remove redundant FDs
        System.out.println("Step 3 : Remove redundant FDs");
        boolean baakiHai = true;

        while(baakiHai) {
            baakiHai = false;
            String key = "", value = "";
            outer:
            for (String s : fd.keySet()) {
                ArrayList<String> rhs = fd.get(s);
                for (String r : rhs) {
                    String closure = getClosureN(fd, s, s, r);
                    if (closure.contains(r)) {
                        baakiHai = true;
                        key = s;
                        value = r;
//                        System.out.println(closure);
                        break outer;
                    }
                }
            }
            if(baakiHai) {
                System.out.println("**Redundant FD found : " + key + " --> " + value);
                System.out.println("---------ReWriting FDs----------");
                printMap(fd);
                ArrayList<String> rhs = fd.get(key);
                rhs.remove(value);
                fd.put(key, rhs);
            }
        }

        System.out.println("---------ReWriting FDs----------");
        printMap(fd);
        System.out.println("\nStep-3 Over!!\n\n");

        //Step - 4 : Merge RHS

        for(String s : fd.keySet()){
            ArrayList<String> rhs = fd.get(s);
            if(rhs.size()>1){
                String merge = "";
                for(String st : rhs){
                    merge+=st;
                }
                rhs.clear();
                rhs.add(merge);
                fd.put(s, rhs);
            }
        }

        System.out.println("*Canonical Cover :\n");
        printMap(fd);

    }

    public static String getClosure(HashMap<String, ArrayList<String>> hm, String s,
                                   String toWorkOn){

        HashSet<String> used = new HashSet<>();

//        System.out.println("\nFinding " + toWorkOn + "+ : ")
        String closure = toWorkOn;
        boolean milega = true;
        while(milega) {
            milega = false;
            for (String st : hm.keySet()) {

                if (closure.contains(st) && !used.contains(st)) {
                    milega = true;
                    ArrayList<String> rhss = hm.get(st);
                    for (String rhs : rhss) {
                        if(!closure.contains(rhs)) closure += rhs;
                        char[] temp = closure.toCharArray();
                        HashSet<Character> hs = new HashSet<>();
                        for(char c : temp) hs.add(c);
                        closure = "";
                        for(char c : hs) closure += c;
                        closure = sortString(closure);
//                        System.out.println(st + " " + closure);
                    }
                    used.add(st);
                }
            }
        }
//        System.out.println("Closure = " + closure);
        return closure;

    }

    public static String getClosureN(HashMap<String, ArrayList<String>> fd, String s,
                                    String toWorkOn, String toNeglect){

        HashSet<String> used = new HashSet<>();

//        System.out.println("\nFinding " + toWorkOn + "+ : ");
        String closure = toWorkOn;
        boolean milega = true;
        while(milega) {
            milega = false;
            for (String st : fd.keySet()) {

                if (closure.contains(st) && !used.contains(st)) {
                    milega = true;
                    ArrayList<String> rhss = fd.get(st);
                    for (String rhs : rhss) {
                        if(st.equals(s) && rhs.equals(toNeglect)) continue;

                        if(!closure.contains(rhs)) closure += rhs;
                        closure = sortString(closure);
//                        System.out.println(st + " " + closure);
                    }
                    used.add(st);
                }
            }
        }



//        System.out.println("Closure = " + closure);
        return closure;

    }

    public static String sortString(String s){
        char[] temp = s.toCharArray();
        Arrays.sort(temp);
        s = new String(temp);
        return s;
    }

    public static ArrayList<String> printPowerSet(char []set, int set_size){

        /*set_size of power set of a set with set_size n is (2**n -1)*/
        long pow_set_size = (long)Math.pow(2, set_size);
        int counter, j;
        ArrayList<String> allCombinations = new ArrayList<>();

        /*Run from counter 000..0 to 111..1*/
        for(counter = 0; counter < pow_set_size; counter++) {
            String s = "";
            for(j = 0; j < set_size; j++) {
                /* Check if jth bit in the
                counter is set If set then
                print jth element from set */
                if((counter & (1 << j)) > 0) {
//                    System.out.print(set[j]);
                    s += set[j];
                }
            }
            allCombinations.add(s);
        }
        return allCombinations;
    }

}



