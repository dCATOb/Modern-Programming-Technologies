import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CardDeck {
    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
    public enum Rank { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }
    public static final class Card {
        public final Suit suit;
        public final Rank rank;
        public Card(Suit suit, Rank rank) { this.suit = Objects.requireNonNull(suit); this.rank = Objects.requireNonNull(rank); }
        @Override public boolean equals(Object o) { if (!(o instanceof Card)) return false; Card c = (Card)o; return suit == c.suit && rank == c.rank; }
        @Override public int hashCode() { return Objects.hash(suit, rank); }
        @Override public String toString() { return rankName(rank) + " " + suitName(suit); }
        private static String rankName(Rank rank) {
            switch (rank) {
                case TWO: return "Двойка"; case THREE: return "Тройка"; case FOUR: return "Четвёрка";
                case FIVE: return "Пятёрка"; case SIX: return "Шестёрка"; case SEVEN: return "Семёрка";
                case EIGHT: return "Восьмёрка"; case NINE: return "Девятка"; case TEN: return "Десятка";
                case JACK: return "Валет"; case QUEEN: return "Дама"; case KING: return "Король"; default: return "Туз";
            }
        }
        private static String suitName(Suit suit) {
            switch (suit) {
                case CLUBS: return "трефы"; case DIAMONDS: return "бубны";
                case HEARTS: return "червы"; default: return "пики";
            }
        }
    }
    private final List<Card> cards = new ArrayList<>();
    public CardDeck() { for (Suit s : Suit.values()) for (Rank r : Rank.values()) cards.add(new Card(s, r)); }
    public void shuffle() { Collections.shuffle(cards); }
    public Card deal() { if (cards.isEmpty()) throw new IllegalStateException("В колоде не осталось карт."); return cards.remove(cards.size() - 1); }
    /** Возвращает true, если карта добавлена; false, если такая карта уже есть в колоде. */
    public boolean returnCard(Card card) { Objects.requireNonNull(card); if (cards.contains(card)) return false; cards.add(card); return true; }
    public int size() { return cards.size(); }
    public static void main(String[] args) {
        CardDeck deck = new CardDeck(); deck.shuffle();
        List<Card> hand = new ArrayList<>(); for (int i = 0; i < 5; i++) hand.add(deck.deal());
        System.out.println("Розданные карты: " + hand + " (в колоде осталось " + deck.size() + " карт)");
        System.out.println("Карта возвращена: " + deck.returnCard(hand.get(0)));
        System.out.println("Повторный возврат той же карты принят: " + deck.returnCard(hand.get(0)));
    }
}
