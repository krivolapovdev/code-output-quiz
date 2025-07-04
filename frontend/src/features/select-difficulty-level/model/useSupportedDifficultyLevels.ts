import { useEffect, useState } from "react";
import { quizMetaService } from "@/shared/api/quiz";

export const useSupportedDifficultyLevels = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [supportedDifficultyLevels, setSupportedDifficultyLevels] = useState<
    string[]
  >([]);

  useEffect(() => {
    const fetchLevels = async () => {
      try {
        setLoading(true);
        const data = await quizMetaService.fetchSupportedDifficultyLevels();
        setSupportedDifficultyLevels(data);
      } catch (error) {
        setSupportedDifficultyLevels([]);
        console.error("Failed to fetch supported difficulty levels", error);
      } finally {
        setLoading(false);
      }
    };

    fetchLevels();
  }, []);

  return { loading, supportedDifficultyLevels };
};
