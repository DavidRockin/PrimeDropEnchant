package net.primeux.primedropenchant.payment;

import lombok.Getter;
import lombok.Setter;
import net.primeux.primedropenchant.enchanting.Enchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Transaction
{

	@Getter
	private Map<iPayment, Float> amounts = new HashMap();

	@Getter
	private Map<Enchant, Integer> enchants;

	@Getter
	private Player player;

	@Getter
	@Setter
	private ItemStack itemStack;

	public boolean canAfford = false;

	public Transaction(Map<Enchant, Integer> enchants, Player player)
	{
		this.player = player;
		this.enchants = enchants;
		this.calculate();
	}

	/**
	 * Begins a transaction
	 * @param enchantments
	 * @param player
	 * @return
	 */
	public static Transaction begin(Map<Enchant, Integer> enchantments, Player player)
	{
		return new Transaction(enchantments, player);
	}

	/**
	 * Calculates our total currencies and if the player can afford it
	 */
	protected void calculate()
	{
		this.canAfford = false;

		for (Map.Entry<Enchant, Integer> enchant : this.getEnchants().entrySet()) {
			this.trackAmount(enchant.getKey().getPayment(), enchant.getKey().getPrice(enchant.getValue()));
		}

		if (this.getAmounts().isEmpty()) {
			return;
		}

		for (Map.Entry<iPayment, Float> amnt : this.getAmounts().entrySet()) {
			if (!amnt.getKey().playerCanAfford(this.getPlayer(), amnt.getValue())) {
				return;
			}
		}

		this.canAfford = true;
	}

	/**
	 * Charges the player
	 */
	public void charge()
	{
		for (Map.Entry<iPayment, Float> p : this.getAmounts().entrySet()) {
			p.getKey().chargePlayer(this.getPlayer(), p.getValue());
		}
	}

	/**
	 * Formats all payments to a pretty string
	 * @return
	 */
	public String formatCost()
	{
		String cost = "";
		for (Map.Entry<iPayment, Float> p : this.getAmounts().entrySet()) {
			if (p.getValue() > 0) {
				cost += p.getKey().formatAmount(p.getValue()) + " ";
			}
		}
		return cost;
	}

	/**
	 * Keeps track of a payment amount
	 * @param payment
	 * @param amount
	 */
	protected void trackAmount(iPayment payment, float amount)
	{
		if (!this.amounts.containsKey(payment)) {
			this.amounts.put(payment, 0.0f);
		}
		this.amounts.put(payment, this.amounts.get(payment) + amount);
	}

}
