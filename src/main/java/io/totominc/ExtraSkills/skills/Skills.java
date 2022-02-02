package io.totominc.ExtraSkills.skills;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Skills {
  /**
   * Registry of all registered skills. They can be accessed by their ID.
   */
  private static final Map<String, Skill> REGISTRY = new HashMap<>();

  public static final Skill MINING = new MiningSkill();

  /**
   * Put a new skill inside the skills registry.
   *
   * @param skill New skill to register.
   */
  public static void registerSkill(@NotNull final Skill skill) {
    REGISTRY.put(skill.getID(), skill);
  }

  /**
   * Retrieve a skill by its ID.
   *
   * @param id ID of the skill.
   * @return A skill or null.
   */
  @Nullable
  public static Skill getByID(@NotNull final String id) {
    return REGISTRY.get(id.toUpperCase());
  }

  /**
   * Create a copy of all skills in the registry and returns an ImmutableSet.
   *
   * @return ImmutableSet of a copy of registered skills in the registry.
   */
  public static Set<Skill> values() {
    return ImmutableSet.copyOf(REGISTRY.values());
  }

  /**
   * Initialize each skill. Make sure to call this method if configuration
   * files have been edited and reloaded.
   */
  public static void update() {
    for (Skill skill : Skills.values()) {
      skill.update();
    }
  }
}
