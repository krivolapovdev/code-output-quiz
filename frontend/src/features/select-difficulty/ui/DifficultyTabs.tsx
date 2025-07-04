import { Tabs } from "antd";
import { useQuizStore } from "@/shared/lib/store";
import { useSupportedDifficultyLevels } from "../model/use-supported-difficulty-levels";

export const DifficultyTabs = () => {
  const { levels, loading } = useSupportedDifficultyLevels();
  const { difficultyLevel, setDifficulty } = useQuizStore();

  if (loading) {
    return <div>Loading tabs...</div>;
  }

  const items = levels.map(lvl => ({
    key: lvl,
    label: lvl.charAt(0).toUpperCase() + lvl.slice(1).toLowerCase(),
    style: { padding: "0" }
  }));

  return (
    <div className="flex justify-end">
      <Tabs
        type="card"
        onChange={key => setDifficulty(key)}
        activeKey={difficultyLevel}
        items={items}
        tabBarStyle={{ margin: 0, borderRadius: 0 }}
      />
    </div>
  );
};
