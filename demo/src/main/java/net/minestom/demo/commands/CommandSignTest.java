package net.minestom.demo.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentMessage;
import net.minestom.server.crypto.MessageSignature;
import net.minestom.server.crypto.SignatureValidator;
import net.minestom.server.entity.Player;
import net.minestom.server.message.ChatPosition;
import net.minestom.server.message.Messenger;

public class CommandSignTest extends Command {
    private static final ArgumentMessage message = ArgumentType.Message("message");

    public CommandSignTest() {
        super("sign");

        addSyntax(((sender, context) -> {
            if (sender instanceof Player player) {
                final MessageSignature signature = context.getSignature().signatureOf(message, player.getUuid());
                final SignatureValidator validator = SignatureValidator.from(player);
                Messenger.sendSystemMessage(player,
                        Component.text("Signature details: preview: ")
                                .append(formatBoolean(context.getSignature().signedPreview()))
                                .append(Component.text(", argument: "))
                                .append(format(SignatureValidator.validate(validator, signature, Component.text(context.get(message)))))
                                .append(Component.text(", preview: "))
                                .append(format(SignatureValidator.validate(validator, signature, player.getLastPreviewedMessage()))),
                        ChatPosition.CHAT);
            } else {
                // TODO Handle this case
            }
        }), message);
    }

    private static Component format(boolean valid) {
        return valid ? Component.text("valid", NamedTextColor.GREEN) : Component.text("invalid", NamedTextColor.RED);
    }

    private static Component formatBoolean(boolean value) {
        return value ? Component.text("true", NamedTextColor.GREEN) : Component.text("false", NamedTextColor.RED);
    }
}
