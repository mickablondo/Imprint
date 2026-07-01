package io.github.mickablondo.imprint.model;

import java.util.List;

/**
 * A record representing a person with basic information.
 *
 * @param firstName the first name of the person
 * @param lastName the last name of the person
 * @param age the age of the person
 * @param email the email address of the person
 * @param hobbies a list of hobbies of the person
 */
public record Person(String firstName,
                     String lastName,
                     int age,
                     String email,
                     List<String> hobbies) {}
