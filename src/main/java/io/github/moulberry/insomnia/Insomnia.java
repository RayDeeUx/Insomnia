/**
 * Insomnia.java
 *
 * Insomnia was originally released by Moulberry under
 * the terms of the CC BY 3.0 License:
 * https://github.com/Moulberry/Insomnia
 *
 * This modification to Insomnia is also released under
 * the terms of the CC BY 3.0 License:
 * https://github.com/RayDeeUx/Insomnia
 *
 * For the legal code of the CC BY 3.0 license, see here:
 * https://creativecommons.org/licenses/by/3.0/legalcode.txt
 * ...as well as here:
 * https://creativecommons.org/licenses/by/3.0/legalcode
 *
 * For the human-readable version of the CC BY 3.0 license, see here:
 * https://creativecommons.org/licenses/by/3.0/
 *
 * This modification to Insomnia includes an additional subcommand
 * to either increase or decrease the extent of player skin replacement,
 * and an additional check to skip pixels representing the player skin's
 * hat/secondary layer. Due to the nature of Dream's skin, checking the
 * player's hat/secondary layer is often unnecessary.
 *
 * - Erymanthus | u/RayDeeUx
 */

package io.github.moulberry.insomnia;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import io.github.moulberry.insomnia.mixins.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod(modid = Insomnia.MODID, version = Insomnia.VERSION, clientSideOnly = true)
public class Insomnia {
    public static final String MODID = "insomnia";
    public static final String VERSION = "1.1-REL";

    public static boolean enabled = true;
    public static boolean skipHatLayer = true;
    public static double threshold = 0.7;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        ClientCommandHandler.instance.registerCommand(new SimpleCommand("insomnia", new SimpleCommand.ProcessCommandRunnable() {
            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                if(args.length != 1) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Invalid usage: /insomnia <on/off/threshold/hat>"));
                    return;
                }
                if(args[0].equalsIgnoreCase("on")) {
                    enabled = true;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Removing dream skins..."));
                } else if(args[0].equalsIgnoreCase("off")) {
                    enabled = false;
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Enabling dream skins..."));
                } else if (args[0].equalsIgnoreCase("threshold")) {
                    if (args.length == 1) {
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Invalid usage: /insomnia threshold <number between 1 and 100>"));
                    } else {
                        try {
                            String theThreshold = args[1];
                            int theThresholdAsInt = Integer.parseInt(theThreshold);
                            if (theThresholdAsInt <= 100 && 1 <= theThresholdAsInt) {
                                threshold = (theThresholdAsInt / 100)
                            } else {
                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Invalid usage: /insomnia threshold <number between 1 and 100>"));
                            }
                        } catch (Exception ignored) {
                            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Invalid usage: /insomnia threshold <number between 1 and 100>"));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("hat")) {
                    if (skipHatLayer) {
                        skipHatLayer = false;
                    } else {
                        skipHatLayer = true;
                    }
                        }
                    }
                }
            }
            if(totalNeonGreen/(float)totalPixels > threshold) {
                dreamSkins.put(uuid, true);
                return true;
            }

        } catch(IOException ignored) {
            return false;
        }
        dreamSkins.put(uuid, false);
        return false;
    }


    private static HashMap<UUID, Boolean> dreamSkins = new HashMap<>();

}
