/*
 * For a detailed explanation regarding each configuration property, visit:
 * https://jestjs.io/docs/configuration
 */

export default {
  transform: {},
  coverageProvider: "v8",
  testEnvironment: "jest-environment-node",

  testMatch: ["**/*.test.js", "**/*.spec.js"],
  preset: "@shelf/jest-mongodb",
  watchPathIgnorePatterns: ["node_modules", "output", "globalConfig"],
  moduleNameMapper: {
    "^@utils(.*)$": "<rootDir>/src/utils$1"
  }
}
