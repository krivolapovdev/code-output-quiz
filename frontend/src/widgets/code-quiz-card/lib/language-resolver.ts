export const prismLanguageResolver = {
  resolve: (lang: string): string => {
    const aliases: Record<string, string> = {
      postgresql: "sql"
    };

    const normalized = lang.trim().toLowerCase();

    return aliases[normalized] ?? normalized;
  }
};
