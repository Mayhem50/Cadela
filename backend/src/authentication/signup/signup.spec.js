describe("Signup", () => {
  it("Save credential", () => {
    expect(credentialRepository).toHaveBeenCalledWith(credential)
  })
})
