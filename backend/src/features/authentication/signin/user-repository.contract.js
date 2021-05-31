export const USER_EMAIL = "any_email@mail.com"
export const USER_ID = "any_user_id"

export const UserRepositoryContract = (repoUnderTest, beforeEach, afterAll) =>
  describe("User Repository Contract", () => {
    beforeEach && beforeEach()
    afterAll && afterAll()
    it("Return user by email", async () => {
      const userRepository = repoUnderTest
      const user = await userRepository.getByEmail(USER_EMAIL)
      const expectedUser = {
        id: user.id,
        password: "any_password",
        email: USER_EMAIL
      }

      expect(user).toMatchObject(expectedUser)
    })
  })
