package cmd

import (
	"github.com/spf13/cobra"
)

var (
	rootCmd = &cobra.Command{
		Short: "This application simply plays with Cobra",
	}
)

// Execute executes the root command.
func Execute() error {
	return rootCmd.Execute()
}
