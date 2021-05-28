export const UserRepositoryContract = (repoUnderTest, USER_ID) =>
  describe("User Repository Contract", () => {
    it("Return user by email", async () => {
      const userRepository = repoUnderTest
      const email = "any_email@mail.com"
      const user = await userRepository.getByEmail(email)
      const expectedUser = { id: USER_ID, password: "any_password", email }

      expect(user).toEqual(expectedUser)
    })
  })
