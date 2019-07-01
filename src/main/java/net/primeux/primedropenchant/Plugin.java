package net.primeux.primedropenchant;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.primeux.primedropenchant.payment.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin
{

	@Getter
	private Economy economy;

	@Getter
	private PaymentHandler paymentHandler = new PaymentHandler();

	@Override
	public void onLoad()
	{
		this.paymentHandler.add(new MoneyPayment(this));
		this.paymentHandler.add(new ExperiencePayment(this));
	}

	@Override
	public void onEnable()
	{
	}

	@Override
	public void onDisable()
	{
	}

}
