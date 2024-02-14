package stock.business;

import suppliers.business.Supplier;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static boolean isFieldEmpty(String str)
    {
        return (str == null | str.length() == 0);
    }

    public static boolean isLegalId(String str)
    {
        return str != null && str.matches("\\d{9}");
    }

    public static boolean isLegalName(String str)
    {
        return str.matches("[a-zA-Z ]+");
    }

    public static boolean isLegalBankAccountNumber(String str)
    {
        return str.matches("\\d{3}-\\d{5}\\/\\d{2}"); // A legal bank account number is ddd-ddddd/dd
    }

    public static boolean isLegalPhoneNumber(String str)
    {
        return str.matches("\\d{3}-\\d{7}"); // A legal phone number is ddd-ddddddd
    }

    public static boolean isLegalEmail(String str)
    {
        return str.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isStringLengthBetween(String str, int lower, int higher)
    {
        return !isFieldEmpty(str) && str.length()>=lower & str.length()<=higher;
    }

    public static boolean isLegalUsername(String username)
    {
        return isStringLengthBetween(username, 4, 20);
    }

    public static boolean isLegalPassword(String password)
    {
        // A legal password contains 6-20 characters, at least one upper case letter, one lower cases letter and one digit
        return isStringLengthBetween(password, 6, 20) & password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    }

    private static <T> Set<Set<T>> powerSet(Set<T> base) {
        Set<Set<T>> pset = new HashSet<Set<T>>();
        if (base.isEmpty()) {
            pset.add(new HashSet<T>());
            return pset;
        }
        List<T> list = new ArrayList<T>(base);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            pset.add(newSet);
            pset.add(set);
        }

        return pset;
    }

    public static <T> Map<Integer, Set<Set<Set<T>>>> findAllPartitions(Set<T> base) {
        Map<Integer, Set<Set<Set<T>>>> parts = new HashMap<>(); // the partitions that are created
        Set<Set<T>> pow = powerSet(base); // the power set of the input set
        if (pow.size() > 1)
            pow.remove(new HashSet<T>());//remove the empty set if it's not the only entry in the power set

        for (Set<T> set : pow) {
            Set<Set<T>> current = new HashSet<Set<T>>();
            current.add(set);
            findPartSets(base, parts, pow, current);
        }

        return parts;
    }

    private static <T> void findPartSets(Set<T> base, Map<Integer, Set<Set<Set<T>>>> parts, Set<Set<T>> pow, Set<Set<T>> current) {
        int maxLen = base.size() - deepSize(current);
        if (maxLen == 0) // the current partition is full -> add it to parts
        {
            int partitionSize = current.size();
            Set<Set<Set<T>>> partitions;
            if(parts.containsKey(partitionSize))
                partitions = parts.get(partitionSize);
            else
                partitions = new HashSet<>();
            partitions.add(current);
            parts.put(partitionSize, partitions);

            return; // no more can be added to current -> stop the recursion
        }
        else
        {
            for (int i = 1; i <= maxLen; i++) //for all possible lengths
            {
                for (Set<T> set : pow) // for every entry in the power set
                {
                    if (set.size() == i) //the set from the power set has the searched length
                    {
                        if (!anyInDeepSet(set, current)) // none of set is in current
                        {
                            Set<Set<T>> next = new HashSet<Set<T>>();
                            next.addAll(current);
                            next.add(set); // next = current + set
                            findPartSets(base, parts, pow, next);
                        }
                    }
                }
            }
        }
    }

    private static <T> int deepSize(Set<Set<T>> set) {
        int deepSize = 0;
        for (Set<T> s : set) {
            deepSize += s.size();
        }
        return deepSize;
    }

    private static <T> boolean anyInDeepSet(Set<T> set, Set<Set<T>> current) {
        boolean containing = false;

        for (Set<T> s : current) {
            for (T item : set) {
                containing |= s.contains(item);
            }
        }

        return containing;
    }

    public static Set<Integer> ObjectSetToIntSet(Set<Object> productIds)
    {
        Set<Integer> res = new HashSet<>();
        for(Object o : productIds)
            res.add((Integer) o);
        return res;
    }

    public static List<Supplier> ObjectSetToSupplierSet(Set<Object> suppliers)
    {
        List<Supplier> res = new ArrayList<>();
        for(Object o : suppliers)
            res.add((Supplier) o);
        return res;
    }

    public static Map<Integer, Integer> mergeProductsMap(Map<Integer, Integer> m1, Map<Integer, Integer> m2)
    {
        for(Integer i : m2.keySet())
        {
            if(m1.containsKey(i))
            {
                m1.put(i, m1.get(i) + m2.get(i));
            }
            else
                m1.put(i, m2.get(i));
        }

        return m1;
    }


    public static Map<String, Map<Integer, Integer>> mergeMap(Map<String, Map<Integer, Integer>> m1, Map<String, Map<Integer, Integer>> m2)
    {
        for(String supplierId : m2.keySet())
        {
            if(m1.containsKey(supplierId))
            {
                for(int k : m2.get(supplierId).keySet())
                    m1.get(supplierId).put(k, m2.get(supplierId).get(k));
            }
            else
                m1.put(supplierId, m2.get(supplierId));
        }

        return m1;
    }

}
