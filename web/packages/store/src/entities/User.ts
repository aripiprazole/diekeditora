/**
 * User entity
 */
export class User {
  /**
   * Creates user instance
   * @param name
   * @param username
   * @param email
   * @param birthday
   * @param createdAt
   * @param updatedAt
   * @param deletedAt
   */
  public constructor(
    public readonly name: string,
    public readonly username: string,
    public readonly email: string,
    public readonly birthday: Date,
    public readonly createdAt: Date,
    public readonly updatedAt: Date | null,
    public readonly deletedAt: Date | null,
  ) {}
}
