import java.util.*;

public class Solution {

    private HashSet<BannedSet> bannedSets;

    public int solution(String[] user_id, String[] banned_id) {
        HashSet<String> users = new HashSet<>(Arrays.asList(user_id));

        HashSet<String>[] bannedList = new HashSet[banned_id.length];
        for (int i = 0; i < banned_id.length; i++){
            bannedList[i] = new HashSet<>();
            for (String user : user_id)
                if (match(banned_id[i], user)) bannedList[i].add(user);
        }

        Arrays.sort(bannedList, Comparator.comparingInt(HashSet::size));

        bannedSets = new HashSet<>();
        countCombNum(new LinkedList<>(), users, bannedList, bannedList.length, 0);

        return bannedSets.size();
    }

    // fr*d*
    private static boolean match(String bannedRegex, String user) {
        String regex = bannedRegex.replaceAll("\\*", ".");

        return user.matches(regex);
    }

    private void countCombNum(LinkedList<String> banned, HashSet<String> users, HashSet<String>[] bannedList, int depth, int i) {
        if (i == depth) {
            bannedSets.add(new BannedSet(banned));
            return;
        }

        for (String bannedId : bannedList[i]) {
            if (users.contains(bannedId)) {
                users.remove(bannedId);
                banned.add(bannedId);
                countCombNum(banned, users, bannedList, depth, i+1);
                users.add(bannedId);
                banned.remove(bannedId);
            }
        }
    }

    static class BannedSet {
        HashSet<String> set;
        BannedSet(List<String> list) {
            set = new HashSet<>(list);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BannedSet)) return false;
            BannedSet bannedSet = (BannedSet) o;

            if (set.size() != bannedSet.set.size())
                return false;

            for (String entry : set) {
                if (!bannedSet.set.contains(entry))
                    return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(set);
        }
    }

    public static void main(String[] args) {
        String[] users = {"frodo", "fradi", "crodo", "abc123", "frodoc"};
        String[] banneds = {"*rodo", "*rodo", "******"};
        int r = new Solution().solution(users, banneds);
        System.out.println(r);
    }
}