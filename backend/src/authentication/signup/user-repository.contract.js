export const COMPLETE_USER = {
  firstName: "John",
  lastName: "McLane",
  email: "any_email@mail.com",
  password: "any_password"
}

export const UserRepositoryContract = (repoUnderTest) =>
  describe("User Repository", () => {
    it("Save user and return an unique id", async () => {
      const userRepository = repoUnderTest

      const id1 = await userRepository.save(COMPLETE_USER)
      const id2 = await userRepository.save({
        ...COMPLETE_USER,
        email: "another_email@mail.com"
      })
      expect(id1).not.toEqual(id2)
    })

    it("Return user by email witout password", async () => {
      const userRepository = repoUnderTest

      await userRepository.save(COMPLETE_USER)
      const user = await userRepository.getByEmail(COMPLETE_USER.email)
      const expectedUser = { ...COMPLETE_USER }
      delete expectedUser.password

      expect(user).toEqual(expectedUser)
    })
  })
